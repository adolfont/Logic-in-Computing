homem(joao).
homem(jose).
homem(pedro).

curitibano(joao).

mortal(X):-homem(X).
brasileiro(X):-homem(X),curitibano(X).



