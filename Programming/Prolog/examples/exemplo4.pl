:- dynamic p/0, a/0, b/0, c/0.
%só para ilustrar o uso do dynamic
%fatos
c.
%regras
p:-a,!,b.
p:-c.

