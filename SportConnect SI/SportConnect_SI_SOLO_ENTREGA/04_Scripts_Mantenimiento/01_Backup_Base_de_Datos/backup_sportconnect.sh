#!/bin/bash

FECHA=$(date +"%Y-%m-%d_%H-%M-%S")
BACKUP_DIR="/home/ubuntu/backups"
DB_NAME="sportconnect"
CONTAINER_NAME="sportconnect_mysql"
BACKUP_FILE="$BACKUP_DIR/${DB_NAME}_backup_$FECHA.sql.gz"

mkdir -p "$BACKUP_DIR"

echo "Iniciando copia de seguridad de la base de datos $DB_NAME..."

sudo docker exec "$CONTAINER_NAME" mysqldump -u root -proot123 "$DB_NAME" | gzip > "$BACKUP_FILE"

if [ $? -eq 0 ]; then
    echo "Copia de seguridad creada correctamente:"
    echo "$BACKUP_FILE"
else
    echo "Error al crear la copia de seguridad"
fi