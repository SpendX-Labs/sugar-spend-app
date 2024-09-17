# Use Nginx to serve the built application
FROM nginx:alpine

# Copy the local build folder to the Nginx container
COPY ./build /usr/share/nginx/html

# Expose port 80
EXPOSE 80

# Start Nginx
CMD ["nginx", "-g", "daemon off;"]