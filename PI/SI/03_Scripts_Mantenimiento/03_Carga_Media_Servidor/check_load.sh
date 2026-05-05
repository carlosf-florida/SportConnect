#!/bin/bash

LIMITE=1.00
CARGA=$(uptime | awk -F'load average:' '{ print $2 }' | cut -d, -f1 | xargs)

echo "Comprobando carga media del servidor..."
echo "Carga media actual: $CARGA"

COMPARACION=$(echo "$CARGA > $LIMITE" | bc -l)

if [ "$COMPARACION" -eq 1 ]; then
    echo "ALERTA: La carga media del servidor supera el límite de $LIMITE."
else
    echo "Estado correcto: la carga media está dentro del límite."
fi