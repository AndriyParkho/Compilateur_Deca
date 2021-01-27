#!/usr/bin/env python3

#Programme qui compile les tests de perf de parcours de liste chainée et renvoie les graphes de temps d'exécution en fonction de la taille de la liste

import os
import subprocess
import matplotlib.pyplot as plt
import pathlib
from shutil import copyfile

def change(file, i):
    new  = open(file[:-5]+"Buffer"+file[-5:], 'w')
    old = open(file, 'r')
    for index, line in enumerate(old):
        if index != 11:
            new.write(line)
        else:
            new.write("\tint tailleList = " + str(i*100) + ";\n")


X=[]
Y1=[]
Y2=[]

for i in range(1,21):
    X.append(i*100)
    change(str(pathlib.Path().absolute()) + "/../deca/codegen/perf/AvecObjet/ParcoursListeChaine.deca", i)
    execution = subprocess.run(["./CompilateurExecuteur.sh", "src/test/deca/codegen/perf/AvecObjet/ParcoursListeChaineBuffer.deca"], stdout=subprocess.PIPE)
    Y1.append(int(execution.stdout.split()[-1]))
    change(str(pathlib.Path().absolute()) + "/../deca/codegen/perf/AvecObjet/DoubleParcoursListeChaine.deca", i)
    execution = subprocess.run(["./CompilateurExecuteur.sh", "src/test/deca/codegen/perf/AvecObjet/DoubleParcoursListeChaineBuffer.deca"], stdout=subprocess.PIPE)
    Y2.append(int(execution.stdout.split()[-1]))

plt.plot(X, Y1)
plt.title("Evolution du temps d'exécution d'un parcours de liste chainée")
plt.xlabel("Taille de la liste")
plt.ylabel("Temps d'exécution")
plt.savefig("ParcoursListeChaine.png")

plt.plot(X, Y2)
plt.title("Evolution du temps d'exécution d'un double parcours de liste chainée")
plt.xlabel("Taille de la liste")
plt.ylabel("Temps d'exécution")
plt.savefig("DoubleParcoursListeChaine.png")

subprocess.run(["rm", str(pathlib.Path().absolute()) + "/../deca/codegen/perf/AvecObjet/ParcoursListeChaineBuffer.deca"])
subprocess.run(["rm", str(pathlib.Path().absolute()) + "/../deca/codegen/perf/AvecObjet/DoubleParcoursListeChaineBuffer.deca"])
os.system('find ' + str(pathlib.Path().absolute()) + '/../deca/codegen/perf/AvecObjet/ -name "*.ass" -type f -delete')

