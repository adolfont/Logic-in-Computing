% Program to translate a propositional formula to clausal form
%
% by Jomi Fred Hubner
% USP/PhD
% Course: Logical Foundations of AI
%
% april 1999
%
% to run with SWI-Prolog
%    swipl -g t1 -f transform.pl


% Consult another program that defines the conectives
% for propositinal calculus
:- initialization(consult('truthTable.pl')).

t1 :- transCNF(((q & p) -> (q v r) & (x -> y))).
t2 :- transCNF(~((a->b)&(b->c)->(a->c))).
t3 :- transCNF( a -> b).
t4 :- transCNF( ~(a & b) ).
t5 :- transCNF( a v (b & c) ).


transCNF(F) :-
      translate(F,L,Trace),
      write('Formula:'), writeWff(F), nl,nl,
      write('Translation trace (F ---> F'' by Rule):'),nl,
      writeTraceList(1,Trace),nl,
      write('Conjunctive normal form:'), nl,
      writeSeqClauses(L,' & '),nl,nl,
      write('Clausal form ({<positive clauses>},{negative clauses)}):'),nl,
      writeInClausalForm(L),nl,
      (horn(L), write('this formula can be translate into Horn clauses');
       write('this formula can not be translate into Horn clauses')),nl,
      !.


writeInClausalForm([]).
writeInClausalForm([C|T]) :-
  selectPosClauses(C,PC),
  selectNegClauses(C,NC),
  write('{'),writeSeqClauses(PC,','), write('},{'),
  writeSeqClauses(NC,','), write('}'),write('  --->  '),
  writeSeqClauses(PC,';'), write(' :- '), writeSeqClauses(NC,','), nl,
  writeInClausalForm(T).


selectPosClauses(~C,[]) :- atomic(C), !.
selectPosClauses(C,[C]) :- atomic(C), !.
selectPosClauses(A v B,P) :-
   selectPosClauses(A,P1),
   selectPosClauses(B,P2),
   append(P1,P2,P), !.

selectNegClauses(~C,[C]) :- atomic(C), !.
selectNegClauses(C,[]) :- atomic(C), !.
selectNegClauses(A v B,P) :-
   selectNegClauses(A,P1),
   selectNegClauses(B,P2),
   append(P1,P2,P), !.

writeSeqClauses([],_).
writeSeqClauses([C],_) :- writeWff(C).
writeSeqClauses([C|T],Sep) :- writeWff(C),write(Sep),writeSeqClauses(T,Sep).

writeTraceList(_,[]).
writeTraceList(N,[(F,NF,Rl)|R]) :-
   write(N), write('.       '),
   writeWff(F), nl, write('    ---> '), writeWff(NF), write('   by '), write(Rl), nl,
   N1 is N + 1,
   writeTraceList(N1,R).

horn(ListClauses) :-
    listHorn(ListClauses).
listHorn([]).
listHorn([C|R]) :-
    selectPosClauses(C,PC),
    length(PC,LPos),  LPos < 2,
    listHorn(R).

%---------------------------------------------------------
%
% Translate(+PropositionalFormulae,-ListOfConjuntions,-trace)
%
%---------------------------------------------------------

% Translation for formulae already as conjunction
translate(F & G, T, Tr) :-
   !,
   translate(F,T1,Tr1),
   translate(G,T2,Tr2),
   append(T1,T2,T),
   append(Tr1,Tr2,Tr).

% Try to transform a formula
translate(F,T, [(F,NF,Tr)|Tr2]) :-
   transform(F, NF, Tr), !,
   translate(NF,T,Tr2).

% no more translations are possible
translate(F,[F],[]).


%---------------------------------------------------------
%
% Transform(+Formula1, -Formula2, -Rule)
%     Formula2 is equivalent to Formula1, but it's closer
%     to clausal form
%
%---------------------------------------------------------
transform( A -> B , ~A v B             , elimImp).
transform( A <-> B, (~A v B) & (A v ~B), elimBiImp).

transform( ~(~A)    , A       , elimDouNeg).
transform( ~(A & B) , ~A v ~B , deMorgan).
transform( ~(A v B) , ~A & ~B , deMorgan).

transform( A & B v C , (A v C) & (B v C) , distribution).
transform( A v B & C , (A v B) & (A v C) , distribution).

transform( A v B , A1 v B , T) :- transform(A , A1 , T).
transform( A v B , A v B1 , T) :- transform(B , B1 , T).
transform( ~A    , ~A1    , T) :- transform(A , A1 , T).

