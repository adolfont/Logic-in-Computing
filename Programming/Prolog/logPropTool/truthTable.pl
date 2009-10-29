
%
% Program to print Truth-Tables for propositional formulae
% (version for generating tables to latex)
%
% by Jomi Fred Hübner
% USP/PhD
% Course: Classical Logic
%
% march 1999
%
% to run with SWI-Prolog
%    swipl -g test -f truthTable.pl

:- op(500, fy, ~).
:- op(1000,xfy, &).
:- op(1049,xfy, v).
:- op(1049,xfy, x). % exclusive or
%:- op(1050,xfy, ->).
:- op(1051,xfy, <->).

:- initialization((retract(shortTable); true)).
% comment this line out to show normal truth Table
:- initialization(asserta(shortTable)).

:- initialization(asserta(latex)).
:- initialization((retract(latex); true)).

test :-
	%truthTable(a & ~b & (a -> c) v a <-> c & b).
        %truthTable(~a & ~(b v c)).
	truthTable(a -> ~(b -> c)).
	%truthTable( (a & b) -> (a v b)).

t9 :- truthTable( (((c & (a v ~b)) v (~c & a) v ((c & ~b) & ~c))) <-> (a v (c & ~b))).
t10 :- limpaTex, truthTable( ((b & c) v ((a & ~b) & c) v ((~a & ~b) & c) <-> c) ).


truthTable(Exp) :-
   listAtoms( Exp, LAtoms),
   writeTopLine(Exp, LAtoms),
   writeTable(Exp, LAtoms),
   (latex, write('\\hline \\end{tabular}'); true),
   !, nl.


%**
%**   Writing top Line
%**

% top line for a short table
writeTopLine(Exp, LAtoms) :-
   shortTable,
   (latex, topLatex([_|LAtoms]) ; true),
   writeAtoms(LAtoms),
   writeWff(Exp),
   (latex, write('\\\\ \\hline'); true),
   nl,!.

% top line for a normal table
writeTopLine(Exp, LAtoms) :-
   reverse(LAtoms,RA),
   getSubWff(Exp, RA, AllFrom),
   (latex, topLatex(AllFrom); true),
   reverse(AllFrom, L),
   writeAtoms(L),
   (latex, write('\\\\ \\hline'); true),
   nl, !.

topLatex(AllFrom) :-
   write('\\begin{tabular} {'),
   verNrCol(AllFrom), write('c}  \\hline'), nl.
verNrCol([]).
verNrCol([_|T]) :- write('c'), verNrCol(T).

writeAtoms([]).
writeAtoms([val(A,_)|T]) :- writeWff(A), writeColSep, writeAtoms(T).

% getSubWff(+Exp, +LA, -NLA)
%	Exp: expression to be printed
%	LA: list of expression already printed
%	NLA: list of expression printed after printing Exp

getSubWff(Exp,LA,LA) :- member(val(Exp,_),LA), !.
getSubWff(~ Exp,LA, [val(-Exp,_)|NLA] ) :-
   getSubWff(Exp,LA,NLA).
getSubWff(Exp,LA, [val(Exp,_)|NLA] ) :-
   functor(Exp, _, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),
   getSubWff(E1,LA,NLA1),
   getSubWff(E2,NLA1,NLA).


writeWff(Exp) :- latex, write('$'), writeWffG(Exp), write('$'), !.
writeWff(Exp) :- writeWffG(Exp).

writeWffG(Exp) :- atom(Exp), write(Exp).
writeWffG(~ Exp) :- %write('('), writeOp(~), writeWffG(Exp), write(')').
   writeOp(~), writeWffG(Exp).
writeWffG(Exp) :-
   functor(Exp, F, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),
   write('('),
   writeWffG(E1), write(' '),
   writeOp(F), write(' '),
   writeWffG(E2), write(')'),
   !.


writeOp(F) :- latex, latexOp(F,LF), write(LF), !.

writeOp(<->) :- shortTable,write('<->'), !.
writeOp(->)  :- shortTable,write(' ->'), !.
writeOp(v)   :- shortTable,write(' v '), !.
writeOp(&)   :- shortTable,write(' & '), !.
writeOp(x)   :- shortTable,write(' x '), !.
writeOp(~)   :- shortTable,write('~ '), !.

writeOp(<->) :- write('<->').
writeOp(->)  :- write('->').
writeOp(v)   :- write('v').
writeOp(&)   :- write('&').
writeOp(x)   :- write('x').
writeOp(~)   :- write('~').

latexOp(<->, '\\leftrightarrow ').
latexOp(-> , '\\rightarrow ').
latexOp(v  , '\\lor ').
latexOp(&  , '\\land ').
latexOp(~  , '\\lnot ').
latexOp(x  , '\\otimes ').

writeColSep :- latex, write('&'), !.
writeColSep :- write('|').

%****
%
%  Printing table lines
%
%****

writeTable(Exp, AtomsVal) :-
   writeTableLine(Exp, AtomsVal),
   combination(AtomsVal, NAV),
   writeTableLine(Exp, NAV),
   fail. % to force try another combination
writeTable(_,_).

combination(L,X) :- nextVal(L,X).
combination(L,X) :- nextVal(L,X1), combination(X1,X).

nextVal([val(H,true)], [val(H,false)]) :- !.
nextVal([val(H,true)|T], [val(H,false)|Val]) :-
   allFalse(T),!,
   initialise(T,Val).
nextVal([H|T], [H|Val]) :-
   nextVal(T, Val).


writeTableLine(Exp, ValAtoms) :-
   writeTruthValues(ValAtoms),
   writeTableExpVal(Exp, ValAtoms, _),
   (latex, write(' \\\\'); true),!,
   nl.

writeTruthValues([]) :- !.
writeTruthValues([val(_,TV)|T]) :-
   writeTruthValue(TV), writeColSep,
   writeTruthValues(T).

writeTableExpVal(Exp, ValAtoms, ValAtoms) :-
   member(val(Exp,ExpVal), ValAtoms),
   (shortTable, writeTruthValue(ExpVal); true),
   !.

writeTableExpVal(~ Exp, ValAtoms, NewValAtoms) :-
   shortTable,
   eval((~ Exp), ValAtoms, ExpVal),
   write('('),
   writeTruthValue(ExpVal),
   write(' '),
   writeTableExpVal(Exp, [val(~Exp,ExpVal)|ValAtoms], NewValAtoms),
   write(')'),
   !.

writeTableExpVal(~ Exp, ValAtoms, [val(~Exp,ExpVal)|ValAtoms1]) :-
   writeTableExpVal(Exp, ValAtoms, ValAtoms1),
   eval((~ Exp), ValAtoms1, ExpVal),
   writeTruthValue(ExpVal),  writeColSep,
   !.

writeTableExpVal(Exp, ValAtoms, NewValAtoms) :-
   shortTable,
   functor(Exp, _, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),
   write('('),
   writeTableExpVal(E1, ValAtoms, NAV1),
   write('  '),
   eval(Exp, NAV1, ExpVal),
   writeTruthValue(ExpVal),
   write('  '),
   writeTableExpVal(E2, [val(Exp,ExpVal)|NAV1], NewValAtoms),
   write(')'),
   !.

writeTableExpVal(Exp, ValAtoms, NewValAtoms) :-
   functor(Exp, _, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),

   writeTableExpVal(E1, ValAtoms, NAV1),
   eval(Exp, NAV1, ExpVal),
   writeTableExpVal(E2, [val(Exp,ExpVal)|NAV1], NewValAtoms),
   writeTruthValue(ExpVal),
   writeColSep,
   !.




%
%  Wff evaluation
%	+Exp: a logic proposition expression, ex: a & - b
%	+ValAtoms: value list of values the formulae in current line,
%          ex: [val(a,true), val(a&b,false),...]
%	-ValExp: the result of thr evaluation
%
eval(Exp, ValAtoms, ValExp) :-
   member(val(Exp,ValExp), ValAtoms),
   !.

eval(~ Exp, ValAtoms, ValExp) :-
   eval(Exp,ValAtoms, V1),
   evalTruthTable(~ V1, ValExp).

eval(Exp, ValAtoms, ValExp) :-
   functor(Exp, F, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),
   eval(E1, ValAtoms, V1),
   eval(E2, [val(E1,V1)|ValAtoms], V2),
   Form =.. [F,V1,V2],
   evalTruthTable(Form, ValExp).

evalTruthTable(~ true , false).
evalTruthTable(~ false, true).

evalTruthTable(true  & true,  true) :- !.
evalTruthTable(_     & _   , false).

evalTruthTable(false v false, false) :- !.
evalTruthTable(_     v _    , true).

evalTruthTable(A x A, false) :- !.
evalTruthTable(_ x _, true).

evalTruthTable(true  -> false, false) :- !.
evalTruthTable(_     -> _    , true).

evalTruthTable(A  <-> A,  true) :- !.
evalTruthTable(_  <-> _, false).


%
%  General clauses
%
limpaTex :- retract(latex), fail.
limpaTex.

limpaShortTable :- retract(shortTable), fail.
limpaShortTable.

writeTruthValue(true)  :- write('T').
writeTruthValue(false) :- write('F').

allFalse([]) :- !.
allFalse([val(_,false)|T]) :- allFalse(T).

initialise([],[]) :- !.
initialise([val(H,_)|T1],[val(H,true)|T2]) :- initialise(T1,T2).

listAtoms( E, L) :-  makeListAtoms(E, [], L).

makeListAtoms(Exp, L1, L2) :- atom(Exp), !, addList(val(Exp,true), L1, L2).
makeListAtoms(~ Exp, L1, L2) :- makeListAtoms(Exp, L1, L2).
makeListAtoms(Exp, L1, L2) :-
   functor(Exp, _, 2), % arity = 2
   arg(1, Exp, E1),
   arg(2, Exp, E2),
   makeListAtoms(E1, L1, L3),
   makeListAtoms(E2, L3, L2).


addList(A, [], [A]) :- !.
addList(A, [A|T], [A|T]):- !.
addList(val(A,TVA),[val(B,TVB)|T], L) :-
   name(A, [N]),
   name(B, [M]),
   ( N < M, L = [val(A,TVA),val(B,TVB)|T];
     addList(val(A,TVA),T,L1), L = [val(B,TVB) | L1]).
