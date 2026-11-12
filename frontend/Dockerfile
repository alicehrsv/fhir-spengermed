
FROM node:22-alpine AS build
# Arbeitsverzeichnis im Container
WORKDIR /app
# package.json + package-lock.json kopieren
COPY ./client_falsch/package*.json ./

# Abhängigkeiten installieren
RUN npm ci

# Restliche App kopieren
COPY ./client_falsch ./

# Angular Production Build
RUN npm run build -- --configuration production


FROM nginx:stable-alpine
COPY --from=build /app/dist/client/browser /usr/share/nginx/html
COPY nginx.conf /etc/nginx/conf.d/default.conf
EXPOSE 80
CMD ["nginx", "-g", "daemon off;"]
