#! /bin/bash

# Auteur : gl10
# Version initiale : 16/01/2021

#Programme bash qui lance decac et ima sur des tests .deca et vérifie que la sortie est identique à celle attendue dans le fichier .ans équvalent

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

find ./src/test/deca/OptionsDecac/ -name "*.ass" -type f -delete


decac
echo -e "\e[32mDecac réussi\e[37m"
decac -P ./src/test/deca/OptionsDecac/test1.deca ./src/test/deca/OptionsDecac/test2.deca ./src/test/deca/OptionsDecac/test3.deca
echo -e "\e[32mDecac -P réussi\e[37m"
decac -v ./src/test/deca/OptionsDecac/test1.deca 
echo -e "\e[32mDecac -v réussi\e[37m"
decac -r 10 ./src/test/deca/OptionsDecac/test1.deca
echo -e "\e[32mDecac -r 10 réussi\e[37m"
decac -r 3 ./src/test/deca/OptionsDecac/test1.deca
echo -e "\e[32mDecac -r 3 echec attendu\e[37m"
decac -p -v ./src/test/deca/OptionsDecac/test1.deca
echo -e "\e[32mDecac -p -v echec attendu\e[37m"
decac -b ./src/test/deca/OptionsDecac/test1.deca
echo -e "\e[32mDecac -b echec attendu\e[37m"

find ./src/test/deca/OptionsDecac/ -name "*.ass" -type f -delete

echo -e "\e[37mDone"

