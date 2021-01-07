#! /bin/sh

# Auteur : Andriy
# Version initiale : 07/01/2021

# On teste des fichier valide ou invalide
# On s'inspire du script fourni

# On se place dans le répertoire du projet (quel que soit le
# répertoire d'où est lancé le script) :
cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

# /!\ test valide lexicalement et syntaxiquement
# test_lex peut au choix afficher les messages sur la sortie standard
# (1) ou sortie d'erreur (2). On redirige la sortie d'erreur sur la
# sortie standard pour accepter les deux (2>&1)
if test_lex src/test/deca/syntax/valid/provided/lex_complet.deca 2>&1 \
    | head -n 1 | grep -q 'lex_complet.deca:[0-38]'
then
    echo "Echec inattendu de test_lex"
    exit 1
else
    echo "OK"
fi

# /!\ test invalide lexicalement
# On test une manière d'écrire un include
if test_lex src/test/deca/syntax/invalid/provided/include_lex1.deca 2>&1 \
    | grep -q 'include_lex1.deca:11'
then
    echo "Echec attendu pour test_lex"
else
    echo "Erreur non detectee par test_lex pour include_lex1.deca"
    exit 1
fi

# /!\ test invalide lexicalement
# On test une manière d'écrire un include
if test_lex src/test/deca/syntax/invalid/provided/include_lex2.deca 2>&1 \
    | grep -q 'include_lex2.deca:11'
then
    echo "Echec attendu pour test_lex"
else
    echo "Erreur non detectee par test_lex pour include_lex2.deca"
    exit 1
fi