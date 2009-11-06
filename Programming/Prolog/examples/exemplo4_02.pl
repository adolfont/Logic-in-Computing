%dynamic faz espécie de "declaração" dos predicados
:- dynamic p/0, a/0, b/0, c/0, d/0, e/0, f/0, g/0, h/0.

%fatos
a.
b.
%c.
%d.
e.
f.


%regras
p:-a,b,c,!,d,e.
p:-f.
p:-g,h.
