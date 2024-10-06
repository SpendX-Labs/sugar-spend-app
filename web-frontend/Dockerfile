# Use an official Node.js runtime as a parent image
FROM node:18-alpine AS production

# Set the working directory
WORKDIR /app

# Copy the package.json and package-lock.json to the container
COPY package*.json ./

# Install production dependencies
RUN npm install --only=production

# Copy the locally built `.next` folder to the container
COPY .next .next

# Expose port 3000 (the default Next.js port)
EXPOSE 3000

# Set the environment to production
ENV NODE_ENV=production

# Start the Next.js app
CMD ["npm", "start"]