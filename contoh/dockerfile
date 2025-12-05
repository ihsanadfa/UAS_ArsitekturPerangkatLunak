# Menggunakan image Nginx sebagai base image
FROM nginx:alpine

# Menyalin file website (HTML, CSS, dan JavaScript) ke direktori Nginx
COPY . /usr/share/nginx/html

# Expose port 80 (Nginx default)
EXPOSE 80

# Tidak perlu menjalankan perintah CMD karena Nginx secara otomatis akan berjalan
