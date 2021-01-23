#! /bin/bash

# Auteur : gl10
# Version initiale : 23/01/2021

#Programme bash qui lance decac et ima un test entré en ligne de commande et affiche le résultat généré

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

src/main/bin/decac -d $1
fichier=$1
fichier="${fichier%.deca}.ass"
echo "--------- Resultat : ---------"
ima -s $fichier
exit 1

