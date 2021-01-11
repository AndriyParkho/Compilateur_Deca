#! /bin/sh

# Auteur : gl10
# Version initiale : 11/01/2021

# Test de la vérification contextuelle sans objet.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


for test_courant in ./src/test/deca/context/valid/provided/ContextSansObjet/*.deca; do
  if  test_context test_courant 2>&1 | grep -q -e 'Exception'
  then
	  echo "\e[31mEchec inattendu de $test_courant"
	  exit 1
  else
	  echo "\e[32mSucces attendu de $test_courant"
  fi
done
