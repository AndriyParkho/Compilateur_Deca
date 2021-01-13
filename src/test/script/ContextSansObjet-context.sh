#! /bin/sh

# Auteur : gl10
# Version initiale : 11/01/2021

# Test de la vérification contextuelle sans objet.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"

echo "Tester les .deca [V] valid, [I] invalid ou [2] les deux ? :"
index_file=1
read mode

if [ $mode = "V" ]
then
	number_of_files=$(ls -Rl ./src/test/deca/context/valid/provided/ContextSansObjet | grep .\\.deca | wc -l)	
	for test_courant in ./src/test/deca/context/valid/provided/ContextSansObjet/*/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[31m$index_file/$number_of_files	Echec inattendu de $test_courant"
			exit 1
		else
			echo "\e[32m$index_file/$number_of_files	Succes attendu de $test_courant"
		fi
		index_file=$((index_file+1))
	done
elif [ $mode = "I" ]
then
	number_of_files=$(ls -Rl ./src/test/deca/context/invalid/provided/ContextSansObjet | grep .\\.deca | wc -l)
	for test_courant in ./src/test/deca/context/invalid/provided/ContextSansObjet/*/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[32m$index_file/$number_of_files	Echec attendu de $test_courant"
		else
			echo "\e[31m$index_file/$number_of_files	Succes inattendu de $test_courant"
			exit 1
		fi
		index_file=$((index_file+1))
	done
elif [ $mode = "2" ]
then
	echo "\e[37mvalid"
	number_of_files=$(ls -Rl ./src/test/deca/context/valid/provided/ContextSansObjet | grep .\\.deca | wc -l)
	for test_courant in ./src/test/deca/context/valid/provided/ContextSansObjet/*/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[31m$index_file/$number_of_files	Echec inattendu de $test_courant"
			exit 1
		else
			echo "\e[32m$index_file/$number_of_files	Succes attendu de $test_courant"
		fi
		index_file=$((index_file+1))
	done
	echo "\e[37minvalid"
	number_of_files=$(ls -Rl ./src/test/deca/context/invalid/provided/ContextSansObjet | grep .\\.deca | wc -l)
	index_file=1
	for test_courant in ./src/test/deca/context/invalid/provided/ContextSansObjet/*/*.deca; do
		if test_context $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo "\e[32m$index_file/$number_of_files	Echec attendu de $test_courant"
		else
			echo "\e[31m$index_file/$number_of_files	Succes inattendu de $test_courant"
			exit 1
		fi
		index_file=$((index_file+1))
	done
else
	echo "Arguments acceptes : V, I ou 2"
fi

echo "\e[37mDone"
