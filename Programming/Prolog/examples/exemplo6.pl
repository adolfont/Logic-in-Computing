
% Regras:
% Todos os homens sao mortais
%mortal(X) :-  homem(X).
mortal(X) :-  homem(X),
    writef( '%w%w%w' , [ 'Sim, ' , X, ' é mortal ' ] ) .

mortal(maria).

%Fatos:
% Sócrates é um homem
homem(socrates).
%homem(joao).  
%homem(jose).  
