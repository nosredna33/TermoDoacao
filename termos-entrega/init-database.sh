#!/bin/bash
# Script de inicialização do banco de dados SQLite

DB_FILE="termos_entrega.db"

if [ -f "$DB_FILE" ]; then
    echo "Banco de dados já existe. Removendo..."
    rm -f "$DB_FILE"
fi

echo "Criando banco de dados..."
sqlite3 "$DB_FILE" < src/main/resources/db/schema.sql
sqlite3 "$DB_FILE" < src/main/resources/db/municipios.sql
sqlite3 "$DB_FILE" < src/main/resources/db/usuarios.sql

echo "Banco de dados criado com sucesso!"
echo "Usuário admin: admin@saude.gov.br / Admin@123"
