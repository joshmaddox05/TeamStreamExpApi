# Render Deployment Guide

This guide walks you through deploying the TeamStream Experience API to Render.com.

## Prerequisites

1. **GitHub Repository**: Your code must be in a GitHub repository
2. **Render Account**: Sign up at [render.com](https://render.com)
3. **Environment Variables**: Have your production API key ready

## Quick Deploy

### Option 1: One-Click Deploy (Coming Soon)
[![Deploy to Render](https://render.com/images/deploy-to-render-button.svg)](https://render.com/deploy)

### Option 2: Manual Setup

#### Step 1: Create Web Service

1. Go to [Render Dashboard](https://dashboard.render.com/)
2. Click **"New +"** → **"Web Service"**
3. Connect your GitHub repository: `TeamStreamExpApi`
4. Configure the service:
   - **Name**: `teamstream-experience-api`
   - **Environment**: `Docker`
   - **Region**: Choose your preferred region
   - **Branch**: `main`
   - **Dockerfile Path**: `./Dockerfile`

#### Step 2: Configure Environment Variables

In the Render dashboard, add these environment variables:

**Public Variables:**
```bash
SPRING_PROFILES_ACTIVE=prod
JAVA_OPTS=-Xmx512m -Xms256m
AGORA_APP_ID=a111a0c2193f4ecaa04d6e74190f082d
CHANNEL_PREFIX=teamstream
CORS_ALLOWED_ORIGINS=*
```

**Secret Variables** (use Render's secret management):
```bash
AGORA_APP_CERTIFICATE=ae0634149a0747d1807db13fcdeca6ae
API_KEY=your-production-api-key-change-this
```

#### Step 3: Deploy

1. Click **"Create Web Service"**
2. Render will automatically build and deploy your application
3. Monitor the build logs for any issues

## Configuration Details

### Dockerfile Features

- **Multi-stage build**: Optimized for fast builds and small image size
- **Non-root user**: Enhanced security
- **Health checks**: Built-in health monitoring
- **Production-ready**: Optimized JVM settings

### Environment Variables

| Variable | Description | Required |
|----------|-------------|----------|
| `AGORA_APP_ID` | Your Agora application ID | Yes |
| `AGORA_APP_CERTIFICATE` | Your Agora certificate (secret) | Yes |
| `API_KEY` | API authentication key (secret) | Yes |
| `SPRING_PROFILES_ACTIVE` | Spring profile (set to `prod`) | Yes |
| `CHANNEL_PREFIX` | Channel name prefix | No (default: teamstream) |
| `CORS_ALLOWED_ORIGINS` | Allowed CORS origins | No (default: *) |

### Health Check

The application includes a health check endpoint at `/api/v1/health` that Render uses to monitor service health.

## Post-Deployment

### 1. Test Your API

Once deployed, your API will be available at:
```
https://your-service-name.onrender.com/api/v1
```

Test the endpoints:
```bash
# Health check
curl https://your-service-name.onrender.com/api/v1/health

# API info
curl https://your-service-name.onrender.com/api/v1/

# Create channel (replace API key)
curl -X POST https://your-service-name.onrender.com/api/v1/channel/create \
  -H "Content-Type: application/json" \
  -H "X-API-Key: your-production-api-key" \
  -d '{
    "userId": "test_user",
    "eventType": "live_stream",
    "uid": 12345,
    "role": "publisher"
  }'
```

### 2. Configure Custom Domain (Optional)

1. In Render dashboard, go to your service
2. Go to **Settings** → **Custom Domains**
3. Add your domain (e.g., `api.teamstream.com`)
4. Configure DNS as instructed by Render

### 3. Monitor Your Service

- **Logs**: View real-time logs in the Render dashboard
- **Metrics**: Monitor CPU, memory, and response times
- **Alerts**: Set up notifications for downtime or errors

## Scaling

### Vertical Scaling
Upgrade your plan in the Render dashboard:
- **Starter**: 512MB RAM, 0.5 CPU
- **Standard**: 2GB RAM, 1 CPU  
- **Pro**: 4GB RAM, 2 CPU

### Horizontal Scaling
Enable autoscaling (Pro plans):
- Set minimum and maximum instances
- Configure scaling triggers

## Troubleshooting

### Common Issues

1. **Build Failures**
   - Check that all files are committed to your repository
   - Verify Dockerfile syntax
   - Check build logs for specific errors

2. **Health Check Failures**
   - Ensure application starts on port specified by `$PORT`
   - Verify health endpoint returns 200 status
   - Check application logs for startup errors

3. **Environment Variable Issues**
   - Verify all required environment variables are set
   - Check that secret variables are properly configured
   - Ensure no typos in variable names

### Debugging

View logs in real-time:
```bash
# Using Render CLI (if installed)
render logs -s your-service-name --tail
```

Or use the Render dashboard logs viewer.

## Security Best Practices

1. **API Keys**: Use strong, unique API keys for production
2. **CORS**: Configure specific allowed origins instead of `*`
3. **HTTPS**: Render provides HTTPS by default
4. **Secrets**: Never commit secrets to your repository
5. **Monitoring**: Set up alerts for unusual API usage

## Cost Optimization

1. **Right-size your plan**: Start with Starter, scale as needed
2. **Sleep on idle**: Starter plans sleep after 15 minutes of inactivity
3. **Monitor usage**: Use Render's metrics to optimize resource usage

## Support

- **Render Documentation**: [render.com/docs](https://render.com/docs)
- **Render Community**: [community.render.com](https://community.render.com)
- **API Issues**: Open an issue in your GitHub repository

## Next Steps

After successful deployment:

1. **Update your frontend**: Point your client applications to the new API URL
2. **Set up monitoring**: Configure external monitoring (e.g., UptimeRobot)
3. **Document the API**: Share the API documentation with your team
4. **Scale as needed**: Monitor usage and upgrade your plan when necessary

Your TeamStream Experience API is now live and ready to handle live streaming requests!
