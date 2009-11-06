:- dynamic ancestral/2, pai/2, mãe/2.

ancestral(X,Y):-pai(X,Y).    %experimente colocar ! no final
ancestral(X,Y):-mãe(X,Y).  %experimente colocar ! no final

pai(joao,maria).
mãe(maria,pedro).
mãe(maria,paulo).
