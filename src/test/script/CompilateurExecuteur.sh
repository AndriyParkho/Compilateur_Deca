#! /bin/bash

# Auteur : gl10
# Version initiale : 23/01/2021

#Programme bash qui lance decac et ima un test entré en ligne de commande et affiche le résultat généré

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

fichier=$1
rm "${fichier%.deca}.ass" 2>/dev/null
if [ -z $2 ]
then
  src/main/bin/decac -d $1
elif [ $2 = "-q" ]
then
  src/main/bin/decac $1
else
  echo "Commande $2 non définie"
  exit 1
fi
fichier="${fichier%.deca}.ass"
echo "--------- Resultat : ---------"
ima -s $fichier
if [ ! -z $2 ] && [ $2 = "-q" ]
then
  rm $fichier
fi
