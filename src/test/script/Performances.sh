#! /bin/bash

# Auteur : gl10
# Version initiale : 25/01/2021

#Programme bash qui lance les tests perf et affiche le nombre de cycles

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

find ./src/test/deca/codegen/perf -name "*.ass" -type f -delete

list_tests=($(find ./src/test/deca/codegen/perf/ -type f | grep \\.deca$ | sort))

for ((i=0 ; i<${#list_tests[@]} ; i++)); do
	
	decac -n "${list_tests[i]}" >/dev/null || exit 1
	fichier_ass=$(find ./src/test/deca/codegen/perf/ -type f | grep \\.ass)
	
	if [ -z "$fichier_ass" ]; then
		echo -e "\e[31m$(($i+1))/${#list_tests[@]}	Fichier .ass non généré pour le test ${list_tests[i]}."
		rm $fichier_ass
		exit 1
	fi
	echo -e "\e[32m$(($i+1))/${#list_tests[@]}	Compilation et exécution de ${list_tests[i]}\e[37m"
	echo "--------- Resultat : ---------"
	ima -s $fichier_ass
	rm $fichier_ass
done
echo -e "\e[37mDone"

