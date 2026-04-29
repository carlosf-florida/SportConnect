#!/bin/bash

echo "Comprobando estado de los contenedores Docker de SportConnect..."
echo "------------------------------------------------------------"

CONTENEDORES=("sportconnect_mysql" "sportconnect_phpmyadmin")

for CONTENEDOR in "${CONTENEDORES[@]}"
do
    ESTADO=$(sudo docker inspect -f '{{.State.Running}}' "$CONTENEDOR" 2>/dev/null)

    if [ "$ESTADO" = "true" ]; then
        echo "OK: El contenedor $CONTENEDOR está funcionando correctamente."
    else
        echo "ALERTA: El contenedor $CONTENEDOR no está funcionando."
    fi
done

echo "------------------------------------------------------------"
echo "Comprobación finalizada."