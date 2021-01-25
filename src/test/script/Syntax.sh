#! /bin/sh

# Auteur : gl10
# Version initiale : 11/01/2021

# Test de la vérification syntaxique sans objet.

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/test/script/launchers:"$PATH"


echo "Tester les .deca [V] valid, [I] invalid ou [2] les deux ? :"
index_file=1
read mode

if [ $mode = "V" ]
then
  number_of_files=$(ls -Rl ./src/test/deca/syntax/valid/ | grep .\\.deca$ | wc -l)
	for test_courant in $(find ./src/test/deca/syntax/valid/ -type f |grep \\.deca$); do
		if test_synt $test_courant 2>&1 | grep -q -e $test_courant -e Circular\ include\ for\ file -e IllegalArgumentException
		then
			echo -e "\e[31m$index_file/$number_of_files	Echec inattendu de $test_courant\e[37m"
			test_synt $test_courant 2>&1
			exit 1
		else
			echo -e "\e[32m$index_file/$number_of_files	Succes attendu de $test_courant"
		fi
		index_file=$((index_file+1))
	done
elif [ $mode = "I" ]
then
  number_of_files=$(ls -Rl ./src/test/deca/syntax/invalid/ | grep .\\.deca$ | wc -l)
	for test_courant in $(find ./src/test/deca/syntax/invalid/ -type f |grep \\.deca$); do
		if test_synt $test_courant 2>&1 | grep -q -e $test_courant -e Circular\ include\ for\ file -e IllegalArgumentException
		then
			echo -e "\e[32m$index_file/$number_of_files	Echec attendu de $test_courant"
		else
			echo -e "\e[31m$index_file/$number_of_files	Succes inattendu de $test_courant\e[37m"
			test_synt $test_courant
			exit 1
		fi
		index_file=$((index_file+1))
	done
elif [ $mode = "2" ]
then
	echo -e "\e[37mvalid"
	number_of_files=$(ls -Rl ./src/test/deca/syntax/valid/ | grep .\\.deca$ | wc -l)
	for test_courant in $(find ./src/test/deca/syntax/valid/ -type f |grep \\.deca$); do
		if test_synt $test_courant 2>&1 | grep -q -e $test_courant
		then
			echo -e "\e[31m$index_file/$number_of_files	Echec inattendu de $test_courant\e[37m"
			test_synt $test_courant 2>&1
			exit 1
		else
			echo -e "\e[32m$index_file/$number_of_files	Succes attendu de $test_courant"
		fi
		index_file=$((index_file+1))
	done
	echo -e "\e[37minvalid"
	index_file=1
	number_of_files=$(ls -Rl ./src/test/deca/syntax/invalid/ | grep .\\.deca$ | wc -l)
	for test_courant in $(find ./src/test/deca/syntax/invalid/ -type f |grep \\.deca$); do
		if test_synt $test_courant 2>&1 | grep -q -e $test_courant -e Circular\ include\ for\ file -e IllegalArgumentException
		then
			echo -e "\e[32m$index_file/$number_of_files	Echec attendu de $test_courant"
		else
			echo -e "\e[31m$index_file/$number_of_files	Succes inattendu de $test_courant\e[37m"
			test_synt $test_courant
			exit 1
		fi
		index_file=$((index_file+1))
	done
else
	echo "Arguments acceptes : V, I ou 2"
	exit 1
fi

echo "\e[37mDone"

