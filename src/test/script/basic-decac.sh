#! /bin/sh

# Test de l'interface en ligne de commande de decac.
# On ne met ici qu'un test trivial, a vous d'en ecrire de meilleurs.

PATH=./src/main/bin:"$PATH"

decac_moins_b=$(decac -b)

if [ "$?" -ne 0 ]; then
    echo "\e[31mERREUR: decac -b a termine avec un status different de zero."
    exit 1
fi

if [ "$decac_moins_b" = "" ]; then
    echo "\e[31mERREUR: decac -b n'a produit aucune sortie"
    exit 1
fi

if echo "$decac_moins_b" | grep -i -e "erreur" -e "error"; then
    echo "\e[31mERREUR: La sortie de decac -b contient erreur ou error"
    exit 1
fi

echo "\e[32mPas de probleme detecte avec decac -b."

# ... et ainsi de suite.
