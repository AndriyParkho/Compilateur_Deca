#! /bin/bash

# Auteur : gl10
# Version initiale : 16/01/2021

#Programme bash qui lance decac et ima sur des tests .deca et vérifie que la sortie est identique à celle attendue dans le fichier .ans équvalent

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

find ./src/test/deca/codegen/valid/ -name "*.ass" -type f -delete

src/main/bin/decac $1
fichier_ass=$(find ./src/test/deca/codegen/valid/ -type f | grep \\.ass)
if [ -z "$fichier_ass"]; then
    echo -e "fichier .ass non généré"
    exit 1
fi
echo "--------- Resultat : ---------"
ima -s $fichier_ass
rm $fichier_ass
exit 1


exit 1

