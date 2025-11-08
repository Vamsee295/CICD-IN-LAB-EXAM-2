# Multi-stage build for React application
FROM node:18-alpine AS build

# Set working directory
WORKDIR /app

# Copy package files
COPY package*.json ./

# Install dependencies
RUN npm ci --only=production

# Copy source code
COPY . .

# Build the application
RUN npm run build

# Production stage with nginx
FROM nginx:alpine

# Install curl for health check
RUN apk add --no-cache curl

# Copy built application
COPY --from=build /app/dist /usr/share/nginx/html

# Copy nginx configuration
COPY nginx.conf /etc/nginx/nginx.conf

# Create non-root user
RUN addgroup -g 1001 quizbuilder && adduser -D -u 1001 -G quizbuilder quizbuilder

# Change ownership
RUN chown -R quizbuilder:quizbuilder /usr/share/nginx/html && \
    chown -R quizbuilder:quizbuilder /var/cache/nginx && \
    chown -R quizbuilder:quizbuilder /var/log/nginx && \
    chown -R quizbuilder:quizbuilder /etc/nginx/conf.d

# Make required directories writable
RUN touch /var/run/nginx.pid && \
    chown -R quizbuilder:quizbuilder /var/run/nginx.pid

# Switch to non-root user
USER quizbuilder

# Expose port
EXPOSE 80

# Health check
HEALTHCHECK --interval=30s --timeout=10s --start-period=30s --retries=3 \
  CMD curl -f http://localhost/ || exit 1

# Start nginx
CMD ["nginx", "-g", "daemon off;"]