#!/bin/bash

# Quiz Builder Deployment Script
# This script orchestrates the complete deployment of Quiz Builder application

set -euo pipefail

# Colors for output
RED='\033[0;31m'
GREEN='\033[0;32m'
YELLOW='\033[1;33m'
BLUE='\033[0;34m'
NC='\033[0m' # No Color

# Configuration
SCRIPT_DIR="$(cd "$(dirname "${BASH_SOURCE[0]}")" && pwd)"
ANSIBLE_DIR="${SCRIPT_DIR}/ansible"
K8S_DIR="${SCRIPT_DIR}/k8s"
LOG_FILE="${SCRIPT_DIR}/deployment.log"

# Default values
ENVIRONMENT="development"
SKIP_CONTAINER_BUILD=false
SKIP_K8S_DEPLOY=false
FORCE_DEPLOY=false

# Functions
log() {
    echo -e "${BLUE}[$(date +'%Y-%m-%d %H:%M:%S')]${NC} $1" | tee -a "${LOG_FILE}"
}

error() {
    echo -e "${RED}[ERROR]${NC} $1" | tee -a "${LOG_FILE}"
    exit 1
}

success() {
    echo -e "${GREEN}[SUCCESS]${NC} $1" | tee -a "${LOG_FILE}"
}

warning() {
    echo -e "${YELLOW}[WARNING]${NC} $1" | tee -a "${LOG_FILE}"
}

# Help function
show_help() {
    cat << EOF
Quiz Builder Deployment Script

Usage: $0 [OPTIONS]

OPTIONS:
    -e, --environment   Environment (development|staging|production) [default: development]
    -s, --skip-build    Skip container build step
    -k, --skip-k8s      Skip Kubernetes deployment step
    -f, --force         Force deployment even if validation fails
    -h, --help          Show this help message

EXAMPLES:
    $0                                    # Full deployment in development mode
    $0 -e production                      # Full deployment in production mode
    $0 -s -k                             # Only validate configuration
    $0 -f                                # Force deployment

EOF
}

# Parse command line arguments
while [[ $# -gt 0 ]]; do
    case $1 in
        -e|--environment)
            ENVIRONMENT="$2"
            shift 2
            ;;
        -s|--skip-build)
            SKIP_CONTAINER_BUILD=true
            shift
            ;;
        -k|--skip-k8s)
            SKIP_K8S_DEPLOY=true
            shift
            ;;
        -f|--force)
            FORCE_DEPLOY=true
            shift
            ;;
        -h|--help)
            show_help
            exit 0
            ;;
        *)
            error "Unknown option: $1"
            ;;
    esac
done

# Validate environment
validate_environment() {
    case $ENVIRONMENT in
        development|staging|production)
            log "Environment set to: $ENVIRONMENT"
            ;;
        *)
            error "Invalid environment: $ENVIRONMENT. Must be one of: development, staging, production"
            ;;
    esac
}

# Check prerequisites
check_prerequisites() {
    log "Checking prerequisites..."
    
    # Check if required tools are installed
    local tools=("ansible" "docker" "kubectl" "helm")
    for tool in "${tools[@]}"; do
        if ! command -v "$tool" &> /dev/null; then
            error "$tool is not installed. Please install $tool first."
        fi
    done
    
    # Check if Ansible collections are installed
    if ! ansible-galaxy collection list | grep -q "kubernetes.core"; then
        warning "Kubernetes Ansible collection not found. Installing..."
        ansible-galaxy collection install kubernetes.core
    fi
    
    # Check Docker daemon
    if ! docker info &> /dev/null; then
        error "Docker daemon is not running. Please start Docker first."
    fi
    
    # Check Kubernetes cluster connection
    if ! kubectl cluster-info &> /dev/null; then
        error "Cannot connect to Kubernetes cluster. Please ensure your cluster is accessible."
    fi
    
    success "Prerequisites check completed"
}

# Build containers
build_containers() {
    if [[ "$SKIP_CONTAINER_BUILD" == true ]]; then
        warning "Skipping container build as requested"
        return
    fi
    
    log "Building containers..."
    
    cd "$ANSIBLE_DIR"
    
    # Run containerization playbook
    if ! ansible-playbook containerize.yml -e "environment=$ENVIRONMENT"; then
        error "Container build failed. Check the logs for details."
    fi
    
    success "Containers built successfully"
}

# Deploy to Kubernetes
deploy_to_kubernetes() {
    if [[ "$SKIP_K8S_DEPLOY" == true ]]; then
        warning "Skipping Kubernetes deployment as requested"
        return
    fi
    
    log "Deploying to Kubernetes..."
    
    cd "$ANSIBLE_DIR"
    
    # Run Kubernetes deployment playbook
    if ! ansible-playbook deploy-k8s.yml -e "environment=$ENVIRONMENT"; then
        if [[ "$FORCE_DEPLOY" == true ]]; then
            warning "Kubernetes deployment failed, but continuing due to --force flag"
        else
            error "Kubernetes deployment failed. Check the logs for details."
        fi
    fi
    
    success "Kubernetes deployment completed"
}

# Health check
health_check() {
    log "Performing health check..."
    
    # Wait for deployments to be ready
    kubectl wait --for=condition=available --timeout=300s deployment/quiz-builder-backend -n quiz-builder
    kubectl wait --for=condition=available --timeout=300s deployment/quiz-builder-frontend -n quiz-builder
    
    # Check service endpoints
    local backend_service=$(kubectl get svc quiz-builder-backend-service -n quiz-builder -o jsonpath='{.spec.clusterIP}')
    local frontend_service=$(kubectl get svc quiz-builder-frontend-service -n quiz-builder -o jsonpath='{.spec.clusterIP}')
    
    log "Backend service IP: $backend_service"
    log "Frontend service IP: $frontend_service"
    
    # Test backend health endpoint
    if kubectl run health-test --image=curlimages/curl --rm -i --restart=Never -- \
       curl -s "http://$backend_service:8080/actuator/health" &> /dev/null; then
        success "Backend health check passed"
    else
        warning "Backend health check failed"
    fi
    
    success "Health check completed"
}

# Display deployment information
show_deployment_info() {
    log "Deployment Information:"
    echo "======================================"
    echo "Application: Quiz Builder"
    echo "Environment: $ENVIRONMENT"
    echo "Namespace: quiz-builder"
    echo ""
    echo "Services:"
    kubectl get svc -n quiz-builder
    echo ""
    echo "Deployments:"
    kubectl get deployments -n quiz-builder
    echo ""
    echo "Pods:"
    kubectl get pods -n quiz-builder
    echo ""
    echo "Ingress:"
    kubectl get ingress -n quiz-builder
    echo "======================================"
}

# Cleanup function
cleanup() {
    log "Cleaning up temporary resources..."
    # Remove any temporary files or resources
    rm -f /tmp/deployment-*.tmp
}

# Main execution
main() {
    log "Starting Quiz Builder deployment..."
    
    # Set up cleanup
    trap cleanup EXIT
    
    # Validate environment
    validate_environment
    
    # Check prerequisites
    check_prerequisites
    
    # Build containers
    build_containers
    
    # Deploy to Kubernetes
    deploy_to_kubernetes
    
    # Health check
    health_check
    
    # Show deployment information
    show_deployment_info
    
    success "Quiz Builder deployment completed successfully!"
    log "Access your application at: https://quiz-builder.example.com"
}

# Run main function
main "$@"