% Program to prove a theorem in propositional calculus using resolution
%
% by Jomi Fred Hubner
%
% may 2005
%
% to run with SWI-Prolog
%    swipl -g r8 -f resolution.pl

:- initialization(consult('transform.pl')).

% tests for the program

r1 :- theorem( (a v ~a)).
r2 :- theorem( ((a->b) & (b ->c) -> (a->c)) ).
r3 :- theorem( ( (a -> b) v (~a) )).
r4 :- theorem( ( (a -> (b -> c)) -> ((a -> b) -> (a -> c))) ).
r5 :- theorem( ((a & b) -> (a v c)) ).
r6 :- theorem( ((a -> b) -> (a <-> b))  ).
r7 :- theorem( ((a & b) -> (a <-> b))  ).

r8 :- theorem( ((~p v ~q v r) & (~p v s) & (~q v s) & p & q) -> r ).

rDet :- theorem( ((~s -> p) & (~(r <-> ~p)) & ~s) -> p ).
rAdv :- theorem( (c -> g) & ~(~g <-> j) & (~o -> ~j) & (o -> (g & m)) & ~m -> ~c).


theorem(F) :-
   limpaTex, limpaShortTable, nl,

   resolution(F,Prove),

   write('Prove:'), nl, writeProve(Prove),
   write('Thus '), writeWff(F), write(' is a theorem.'), nl, !.

theorem(F) :- 
    writeWff(F), write(' is not a theorem.'), nl.

% try to prove F, unifies in Prove the prove
%   . Negations of F (prove by refutation)
%   . translate to CNF (to use resolution inference rule)
%   . search for contradiction
%
resolution(F,Prove) :-
   translate(~F, L, _),

   write('------------------------------'),nl,
   write('Starting resolution for '), writeWff(F), nl,nl,
   write('Negation '), writeWff(~F), nl, 
   write('Clausal form: '), writeSeqClauses(L,' & '),nl,!,

   createClauses(L,L2,1), !,
   reverse(L2,L3),
   search(L3,P),
   reverse(P,Prove).

% creates a list of clause predicates where
% each predicate is c(<number>, <formula>, <from>)
% and each <formula> is a disjunction list
% e.g.: 
%    a v b ==> c(1, [a,b], hyp)
%
createClauses([],[],_).

% do not include tautologies
createClauses([C|R],R2, N) :-
  transOrToList(C,C2), 
  isTautology(C2), 
  createClauses(R,R2,N).

createClauses([C|R],[c(N,CL,hyp)|R2], N) :-
  transOrToList(C,C2), 
  sort(C2,CL),
  N1 is N + 1, 
  createClauses(R,R2,N1).

transOrToList(X v Y, [X|L]) :-
  transOrToList(Y,L).
transOrToList(X,[X]).

isTautology(C) :-
  member(P,C), member(~P, C).
 
writeProve([]).
writeProve([c(N,C,From)|P]) :-
  writef('%3R. %15L %w', [N, C, From]),nl,
  writeProve(P).


%search( [ [c(N,[],R)|Path] | _], [c(N,[],R)|Path]).
%search([ Path|Open], Sol) :-
%   findall(Suc, suc( Path, Suc), Suc2), 
%   %write('F='),write(Suc2),nl,nl,
%   append( Open, Suc2, Open2), % largura
%   %write('Open size is '), length(Open2,N), write(N),nl,
%   search( Open2, Sol).

search( [c(N,[],R)|Path], [c(N,[],R)|Path]).
search( Path, Sol) :-
   next( Path, Suc), 
   search( Suc, Sol).

% identifies contradiction
%   [a]    [~a]
%  -------------
%       []
%
next(Path,[c(Number,[],(N1,N2))|Path]) :-  
  member(c(N1,[A], _), Path), 
  member(c(N2,[~A],_), Path),
  length(Path, Len), Number is Len + 1.

% resolution rule
%   [a,b]    [~a, c]
%  ------------------
%        [b,c]
%
next(Path,[c(Number,R,(N1,N2))|Path]) :- 
  member(c(N1,C1,_), Path), member(A, C1), 
  member(c(N2,C2,_), Path), member(~A,C2), 
  delete(C1,A,R1), 
  delete(C2,~A,R2),
  append(R1,R2,R3), 
  sort(R3,R),                % to remove redundancies 
  \+ isTautology(R),         % to not add tautologies
  \+ member(c(_,R,_), Path), % to not repeat formulae
  length(Path, Len), Number is Len + 1.


prove(Hyp) :-
  findall(SF, db(SF), L),
  makeConjunction(L,CF),
  theorem((CF -> Hyp)).

makeConjunction([F], F).
makeConjunction([F1|T], F1 & FT) :-
  makeConjunction(T,FT).

