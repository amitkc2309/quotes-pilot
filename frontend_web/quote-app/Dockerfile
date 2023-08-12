# use this command to create image- docker build --build-arg REACT_APP_BACKEND_URL="http://localhost:5000" -t amitking2309/quotes-pilot-frontend .
# using docker multistage build
#stage 1
FROM node:17-alpine as builder
# Set the working directory to /app inside the container
WORKDIR /app
# Copy app files
COPY . .
# Install dependencies
RUN npm install --force

ARG REACT_APP_BACKEND_URL
RUN sh create-env-file.sh REACT_APP_BACKEND_URL=$REACT_APP_BACKEND_URL
# debug the .env file
# CMD ["cat", ".env"]

# Build the app
 RUN npm run build

#stage 2
# after this stage the final container will only contain build folder and nothing else
# Bundle static assets with nginx
FROM nginx:1.21.0-alpine as production
WORKDIR /usr/share/nginx/html
RUN rm -rf ./*
# Copy built assets from `builder` image
COPY --from=builder /app/build /usr/share/nginx/html
# Copying our nginx.conf
COPY nginx.conf /etc/nginx/conf.d/default.conf
# Expose port
EXPOSE 80
# Start nginx
CMD ["nginx", "-g", "daemon off;"]