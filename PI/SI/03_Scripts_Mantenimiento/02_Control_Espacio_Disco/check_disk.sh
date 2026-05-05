#!/bin/bash

LIMITE=80
USO=$(df / | tail -1 | awk '{print $5}' | sed 's/%//')

echo "Comprobando espacio en disco..."
echo "Uso actual del disco: $USO%"

if [ "$USO" -ge "$LIMITE" ]; then
    echo "ALERTA: El disco ha superado el $LIMITE% de uso."
else
    echo "Estado correcto: el disco está por debajo del $LIMITE%."
fi