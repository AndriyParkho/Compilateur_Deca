#! /bin/bash

# Auteur : gl10
# Version initiale : 16/01/2021

#Programme bash qui lance decac et ima sur des tests .deca et vérifie que la sortie est identique à celle attendue dans le fichier .ans équvalent

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

src/main/bin/decac -d $1
fichier=$1
fichier="${fichier%.deca}.ass"
echo "--------- Resultat : ---------"
ima -s $fichier
exit 1

