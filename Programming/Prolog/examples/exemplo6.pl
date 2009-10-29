mortal(X) :- % Todos os homens sao mortais
  homem(X),
  writef( '%w%w%w' , [ ' Sim , ' , X, ' é mortal ' ] ) .
homem(socrates). % Sócrates é um homem
