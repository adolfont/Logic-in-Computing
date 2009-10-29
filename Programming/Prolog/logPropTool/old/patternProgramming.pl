%
% Program to run pattern-direct programas
%      (from Ivan Bratko. Prolog, programming for AI, 2nd Ed.)
%
% by Jomi Fred Hubner
% USP/PhD
% Subject: Classical Logic
%
% april 1999
%
% to run with SWI-Prolog

% the rules must be written in this format:
%   [pre-condition patter] ---> [actions to be executed]
%

:- op( 800, xfx, --->).

run :- 
   Cond ---> Action,
   test(Cond),
   execute(Action).


test([]).
test([F|R]) :-
   call(F),
   test(R).

execute([stop]) :- !.  % stop program
execute([]) :- run.    % next execution cycle
execute([F|R]) :-
   call(F),
   execute(R).

replace(A,B) :-
   retract(A),!,assert(B).


