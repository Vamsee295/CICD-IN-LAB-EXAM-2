# Quiz Builder - Kubernetes Deployment Guide

This guide provides comprehensive instructions for deploying the Quiz Builder application using Ansible and Kubernetes.

## Overview

The Quiz Builder application consists of:
- **Backend**: Spring Boot application with MySQL database
- **Frontend**: React application with Vite build system
- **Infrastructure**: Kubernetes cluster with Ingress, SSL/TLS, and auto-scaling

## Prerequisites

### Required Tools
- **Docker**: For containerization
- **Kubernetes Cluster**: Local (minikube/kind) or cloud (EKS/GKE/AKS)
- **Ansible**: For automation and deployment
- **kubectl**: Kubernetes CLI
- **Helm**: Package manager (optional)

### System Requirements
- 4+ CPU cores
- 8GB+ RAM
- 20GB+ available disk space
- Linux/macOS/Windows with WSL

## Quick Start

### 1. Clone and Setup
```bash
git clone <repository-url>
cd QuizBuilder-master
```

### 2. Configure Environment
Edit `ansible/vars.yml` to match your environment:
```yaml
# Registry configuration
container_registry: "your-registry.com"
registry_username: "your-username"
registry_password: "your-password"

# Application settings
app_name: "quiz-builder"
environment: "production"

# Database configuration
db_root_password: "secure-root-password"
db_password: "secure-db-password"

# SSL/TLS settings
ssl_enabled: true
ssl_issuer: "letsencrypt-prod"
```

### 3. Deploy Application
```bash
# Make deployment script executable
chmod +x deploy.sh

# Deploy to development environment
./deploy.sh

# Deploy to production environment
./deploy.sh -e production

# Skip container build (use existing images)
./deploy.sh -s

# Skip Kubernetes deployment
./deploy.sh -k
```

## Detailed Deployment Steps

### Step 1: Build Containers
```bash
cd ansible
ansible-playbook containerize.yml -e "environment=production"
```

This playbook will:
- Build backend Docker image
- Build frontend Docker image
- Scan images for security vulnerabilities
- Push images to container registry

### Step 2: Deploy to Kubernetes
```bash
ansible-playbook deploy-k8s.yml -e "environment=production"
```

This playbook will:
- Create Kubernetes namespace
- Deploy MySQL database
- Deploy backend application
- Deploy frontend application
- Configure Ingress with SSL/TLS
- Set up auto-scaling

### Step 3: Verify Deployment
```bash
# Check deployment status
kubectl get all -n quiz-builder

# Check logs
kubectl logs -f deployment/quiz-builder-backend -n quiz-builder
kubectl logs -f deployment/quiz-builder-frontend -n quiz-builder

# Test endpoints
curl -k https://quiz-builder.example.com/api/health
curl -k https://quiz-builder.example.com
```

## Configuration

### Environment Variables
The application uses the following environment variables:

#### Backend Configuration
```yaml
# Database
SPRING_DATASOURCE_URL: "jdbc:mysql://mysql-service:3306/quizbuilder"
SPRING_DATASOURCE_USERNAME: "quizuser"
SPRING_DATASOURCE_PASSWORD: "${DB_PASSWORD}"

# Application
SPRING_PROFILES_ACTIVE: "production"
SERVER_PORT: "8080"

# Security
JWT_SECRET: "${JWT_SECRET}"
```

#### Frontend Configuration
```yaml
# API Configuration
VITE_API_URL: "https://quiz-builder.example.com/api"
VITE_ENVIRONMENT: "production"
```

### Scaling Configuration
Horizontal Pod Autoscaler (HPA) settings:
```yaml
# Backend scaling
minReplicas: 3
maxReplicas: 10
targetCPUUtilization: 70%
targetMemoryUtilization: 80%

# Frontend scaling
minReplicas: 3
maxReplicas: 10
targetCPUUtilization: 70%
targetMemoryUtilization: 80%
```

## Security Features

### Container Security
- Non-root user execution
- Read-only root filesystem
- Dropped capabilities
- Security context constraints

### Network Security
- Network policies (configurable)
- TLS termination at Ingress
- Service mesh ready (Istio compatible)

### Secrets Management
- Kubernetes Secrets for sensitive data
- External secret management support (Vault, AWS Secrets Manager)

## Monitoring and Observability

### Health Checks
- Liveness probes for all containers
- Readiness probes for traffic routing
- Startup probes for slow-starting containers

### Logging
- Structured JSON logging
- Centralized log aggregation ready
- Log rotation and retention

### Metrics
- Prometheus metrics endpoint
- Custom application metrics
- Infrastructure metrics

## Troubleshooting

### Common Issues

#### Pod Not Starting
```bash
# Check pod status
kubectl describe pod <pod-name> -n quiz-builder

# Check events
kubectl get events -n quiz-builder --sort-by='.lastTimestamp'

# Check logs
kubectl logs <pod-name> -n quiz-builder --previous
```

#### Database Connection Issues
```bash
# Check MySQL pod
kubectl get pods -n quiz-builder | grep mysql

# Check MySQL logs
kubectl logs -f deployment/mysql -n quiz-builder

# Test database connection
kubectl run mysql-client --image=mysql:8.0 --rm -it -- \
  mysql -h mysql-service -u quizuser -p
```

#### Ingress Issues
```bash
# Check Ingress status
kubectl get ingress -n quiz-builder

# Check Ingress controller logs
kubectl logs -n ingress-nginx deployment/ingress-nginx-controller

# Test service directly
kubectl port-forward svc/quiz-builder-backend-service 8080:8080 -n quiz-builder
```

### Performance Issues
```bash
# Check resource usage
kubectl top pods -n quiz-builder

# Check HPA status
kubectl get hpa -n quiz-builder

# Scale manually if needed
kubectl scale deployment quiz-builder-backend --replicas=5 -n quiz-builder
```

## Backup and Recovery

### Database Backup
```bash
# Create backup
kubectl exec -it deployment/mysql -n quiz-builder -- \
  mysqldump -u root -p quizbuilder > backup.sql

# Restore backup
kubectl exec -i deployment/mysql -n quiz-builder -- \
  mysql -u root -p quizbuilder < backup.sql
```

### Application State Backup
- Persistent volumes for stateful data
- Regular snapshots (if using cloud storage)
- Configuration backup in Git

## Maintenance

### Updates and Rollbacks
```bash
# Update deployment
kubectl set image deployment/quiz-builder-backend \
  quiz-builder-backend=your-registry.com/quiz-builder-backend:v2.0.0 -n quiz-builder

# Check rollout status
kubectl rollout status deployment/quiz-builder-backend -n quiz-builder

# Rollback if needed
kubectl rollout undo deployment/quiz-builder-backend -n quiz-builder
```

### Cleanup
```bash
# Delete entire deployment
kubectl delete namespace quiz-builder

# Delete specific resources
kubectl delete -f k8s/ -n quiz-builder
```

## Production Considerations

### High Availability
- Multi-zone deployment
- Pod disruption budgets
- Anti-affinity rules

### Disaster Recovery
- Regular backups
- Cross-region replication
- Automated failover

### Compliance
- Audit logging
- Resource quotas
- Network segmentation

## Support

For issues and questions:
1. Check this documentation
2. Review application logs
3. Check Kubernetes events
4. Consult the troubleshooting section

## Contributing

To contribute to the deployment configuration:
1. Test changes in development environment
2. Update documentation
3. Submit pull request with detailed description