#! /bin/bash

# Auteur : gl10
# Version initiale : 16/01/2021

#Programme bash qui compare la sortie après compilation et exécution des programmes .deca et des programmes décompilés associés

cd "$(dirname "$0")"/../../.. || exit 1

PATH=./src/main/bin:../global/bin:"$PATH"

find ./src/test/deca/codegen/valid/ -name "*.ass" -type f -delete
find ./src/test/deca/codegen/valid/ -name "*Dec.deca" -type f -delete

list_tests=($(find ./src/test/deca/codegen/valid/ -type f | grep \\.deca$ | sort))
list_answers=($(find ./src/test/deca/codegen/valid/ -type f | grep \\.ans | sort))

for ((i=0 ; i<${#list_tests[@]} ; i++)); do
    fichier="${list_tests[i]%.deca}.ans"
    if [ ! -f "$fichier" ]; then
	echo "Le fichier ${fichier} n'existe pas"
    fi
done

if [ ${#list_tests[@]} != ${#list_answers[@]} ]; then
	echo "Il manque $((${#list_tests[@]}-${#list_answers[@]})) fichier(s) .ans"
	exit 1
fi



for ((i=0 ; i<${#list_tests[@]} ; i++)); do

	skip=$(grep -e readInt\(\) -e readFloat\(\) -e Skipped\ test -e Skipped\ decompile ${list_tests[i]})	
	if [ ! -z "$skip" ]; then
		echo -e "\e[97m$(($i+1))/${#list_tests[@]}	Test ${list_tests[i]} skipped"
		if [ i = ${#list_tests[@]} ] | [ i = ${#list_answers[@]} ]; then 
			break
		else
			continue
		fi
	fi

	fichier_decompile="${list_tests[i]%.deca}Dec.deca"
	decac -p "${list_tests[i]}" > $fichier_decompile || exit 1
	decac $fichier_decompile
	fichier_ass=$(find ./src/test/deca/codegen/valid/ -type f | grep \\.ass)
	
	if [ -z "$fichier_ass" ]; then
	    echo -e "\e[31m$(($i+1))/${#list_tests[@]}	Fichier .ass non généré pour le test ${list_tests[i]}."
	    rm $fichier_decompile
		rm $fichier_ass
		exit 1
	fi
	$(ima $fichier_ass > buffer)	
	resultat=$(cat buffer)
	attendu=$(cat ${list_answers[i]})

	if [[ "$resultat" == "$attendu" ]]; then
		echo -e "\e[32m$(($i+1))/${#list_tests[@]}	Compilation réussie pour ${list_tests[i]}"
	else
		echo -e "\e[31m$(($i+1))/${#list_tests[@]}	Résultat inattendu de ima pour ${list_tests[i]} :"
		echo -e "\e[97mRésultat = $resultat"
		echo "Attendu = $attendu"
		rm buffer
		rm $fichier_ass
		exit 1
	fi	
	rm buffer
	rm $fichier_ass
	rm $fichier_decompile
done
echo -e "\e[37mDone"

