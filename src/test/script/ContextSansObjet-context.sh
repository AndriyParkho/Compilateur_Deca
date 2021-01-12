#! /bin/sh

# Auteur : gl10
# Version initiale : 11/01/2021

# Test de la vérification contextuelle sans objet.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

echo "Tester les .deca [V] valid, [I] invalid ou [2] les deux ? :"

read mode

if [ $mode = "V" ]
then
	for test_courant in ./src/test/deca/context/valid/provided/ContextSansObjet/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[31mEchec inattendu de $test_courant"
			exit 1
		else
			echo "\e[32mSucces attendu de $test_courant"
		fi
	done
elif [ $mode = "I" ]
then
	for test_courant in ./src/test/deca/context/invalid/provided/ContextSansObjet/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[32mEchec attendu de $test_courant"
		else
			echo "\e[31mSucces inattendu de $test_courant"
			exit 1
		fi
	done
elif [ $mode = "2" ]
then
	echo "\e[37mvalid"
	for test_courant in ./src/test/deca/context/valid/provided/ContextSansObjet/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[31mEchec inattendu de $test_courant"
			exit 1
		else
			echo "\e[32mSucces attendu de $test_courant"
		fi
	done
	echo "\e[37minvalid"
	for test_courant in ./src/test/deca/context/invalid/provided/ContextSansObjet/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[32mEchec attendu de $test_courant"
		else
			echo "\e[31mSucces inattendu de $test_courant"
			exit 1
		fi
	done
else
	echo "Arguments acceptes : V, I ou 2"
fi