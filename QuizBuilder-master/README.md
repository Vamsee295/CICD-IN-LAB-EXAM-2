# Quiz Builder - Cloud-Native Deployment

A comprehensive cloud-native deployment solution for the Quiz Builder application using Ansible, Docker, and Kubernetes.

## 🚀 Quick Start

```bash
# Clone the repository
git clone <repository-url>
cd QuizBuilder-master

# Deploy to development
./deploy.sh

# Deploy to production
./deploy.sh -e production
```

## 📋 Architecture

### Application Components
- **Backend**: Spring Boot REST API with MySQL database
- **Frontend**: React single-page application with Vite
- **Infrastructure**: Kubernetes with auto-scaling and SSL/TLS

### Deployment Stack
- **Containerization**: Docker with multi-stage builds
- **Orchestration**: Kubernetes with Helm charts
- **Automation**: Ansible playbooks
- **Security**: Non-root containers, secrets management, network policies
- **Monitoring**: Health checks, metrics, structured logging

## 🏗️ Project Structure

```
QuizBuilder-master/
├── QuizBuilder-master/          # Application source code
│   ├── backend/                 # Spring Boot backend
│   └── frontend/                # React frontend
├── ansible/                     # Ansible automation
│   ├── containerize.yml         # Docker build playbook
│   ├── deploy-k8s.yml           # Kubernetes deployment playbook
│   ├── inventory.ini            # Server inventory
│   ├── vars.yml                 # Configuration variables
│   └── requirements.yml         # Ansible dependencies
├── k8s/                         # Kubernetes manifests
│   ├── namespace.yaml           # Namespace definition
│   ├── backend-*.yaml           # Backend deployment configs
│   ├── frontend-*.yaml          # Frontend deployment configs
│   ├── mysql-deployment.yaml    # Database deployment
│   ├── ingress.yaml             # Ingress configuration
│   └── hpa.yaml                 # Auto-scaling configuration
├── deploy.sh                    # Main deployment script
├── DEPLOYMENT.md                # Detailed deployment guide
└── README.md                    # This file
```

## 🔧 Configuration

### Environment Variables
Edit `ansible/vars.yml` for your environment:

```yaml
# Container Registry
container_registry: "your-registry.com"
registry_username: "your-username"
registry_password: "your-password"

# Application Settings
app_name: "quiz-builder"
environment: "production"

# Database Configuration
db_root_password: "secure-root-password"
db_password: "secure-db-password"

# SSL/TLS Settings
ssl_enabled: true
domain_name: "quiz-builder.example.com"
```

### Deployment Options

```bash
# Full deployment
./deploy.sh

# Skip container build (use existing images)
./deploy.sh -s

# Skip Kubernetes deployment
./deploy.sh -k

# Force deployment (ignore validation errors)
./deploy.sh -f

# Deploy to specific environment
./deploy.sh -e staging
```

## 🔒 Security Features

### Container Security
- ✅ Non-root user execution
- ✅ Read-only root filesystem
- ✅ Dropped capabilities
- ✅ Security context constraints
- ✅ Vulnerability scanning with Trivy

### Network Security
- ✅ TLS termination at Ingress
- ✅ Network policies (configurable)
- ✅ Service mesh ready
- ✅ Rate limiting

### Secrets Management
- ✅ Kubernetes Secrets
- ✅ External secret manager support
- ✅ Encrypted configuration

## 📊 Monitoring & Observability

### Health Checks
- ✅ Liveness probes
- ✅ Readiness probes
- ✅ Startup probes

### Logging & Metrics
- ✅ Structured JSON logging
- ✅ Prometheus metrics
- ✅ Centralized log aggregation ready

### Auto-scaling
- ✅ Horizontal Pod Autoscaler (HPA)
- ✅ CPU-based scaling (70% threshold)
- ✅ Memory-based scaling (80% threshold)
- ✅ Scale range: 3-10 replicas

## 🛠️ Prerequisites

### Required Tools
- Docker (20.10+)
- Kubernetes cluster (1.20+)
- Ansible (2.9+)
- kubectl (1.20+)
- Helm (3.0+) - optional

### System Requirements
- 4+ CPU cores
- 8GB+ RAM
- 20GB+ available disk space

## 📖 Deployment Process

### 1. Containerization
```bash
cd ansible
ansible-playbook containerize.yml
```

Builds secure Docker images:
- Multi-stage builds for optimization
- Security scanning with Trivy
- Image signing and verification

### 2. Kubernetes Deployment
```bash
ansible-playbook deploy-k8s.yml
```

Deploys to Kubernetes:
- Namespace creation
- Database deployment
- Application deployment
- Ingress configuration
- Auto-scaling setup

### 3. Verification
```bash
# Check deployment status
kubectl get all -n quiz-builder

# Check application health
curl -k https://quiz-builder.example.com/actuator/health
```

## 🔍 Troubleshooting

### Common Commands
```bash
# Check pod status
kubectl get pods -n quiz-builder

# View logs
kubectl logs -f deployment/quiz-builder-backend -n quiz-builder

# Check events
kubectl get events -n quiz-builder --sort-by='.lastTimestamp'

# Port forward for testing
kubectl port-forward svc/quiz-builder-backend-service 8080:8080 -n quiz-builder
```

### Debug Mode
```bash
# Enable verbose logging
export ANSIBLE_VERBOSITY=3
./deploy.sh
```

## 🚀 Production Deployment

### Pre-deployment Checklist
- [ ] Configure production variables in `ansible/vars.yml`
- [ ] Set up container registry access
- [ ] Configure SSL/TLS certificates
- [ ] Set up monitoring and alerting
- [ ] Configure backup strategy
- [ ] Test disaster recovery procedures

### High Availability
- Multi-zone deployment
- Pod disruption budgets
- Anti-affinity rules
- Database replication

### Security Hardening
- Network policies
- Pod security policies
- RBAC configuration
- Audit logging

## 📚 Documentation

- **[DEPLOYMENT.md](DEPLOYMENT.md)** - Detailed deployment guide
- **[Ansible Documentation](https://docs.ansible.com/)**
- **[Kubernetes Documentation](https://kubernetes.io/docs/)**
- **[Docker Documentation](https://docs.docker.com/)**

## 🤝 Contributing

1. Fork the repository
2. Create a feature branch
3. Test your changes in development
4. Submit a pull request

## 📄 License

This project is licensed under the MIT License - see the LICENSE file for details.

## 🆘 Support

For issues and questions:
1. Check the [DEPLOYMENT.md](DEPLOYMENT.md) guide
2. Review the troubleshooting section
3. Open an issue in the repository

---

**Happy Deploying! 🎉**