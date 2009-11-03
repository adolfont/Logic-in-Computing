pessoa(ale). /* Alexandra */
pessoa(babe). /* Barbara */
pessoa(rosa).
cor(azul).
cor(amarelo).
cor(vermelho).
cor(lilaz).
cor(verde).
/* este pedaço é importante... pois.... está
dito no problema e define uma cor para cada
moça */
tem_uma_cor(babe,amarelo).
tem_uma_cor(rosa,verde).
tem_uma_cor(ale,azul).
/* aqui pode-se ampliar as restrições */
nao_pode_vestir(babe,verde).
nao_pode_vestir(rosa,azul).
nao_pode_vestir(rosa,lilaz).
veste(X, Cor1, Cor2) :-
pessoa(X),
cor(Cor1),
cor(Cor2),
tem_uma_cor(X,Cor1),
\+(nao_pode_vestir(X,Cor2)) , /* é um fato que deve ser negado */
Cor1 \== Cor2 , /* C1 diferente de C2 */
/* não pode ter amarelo e vermelho com C1 e C2 e vice-versa */
\+(
((Cor1 = vermelho) , /* C1 tem que ser diferente de Vermelho */
(Cor2 = amarelo)) /* idem para amarelo.... */
; /* ; ou */
((Cor1 = amarelo) , /* , e ... o contrário */
(Cor2 = vermelho))
).
ache_tudo :-
veste(X,C1,C2),
write(X),
write(' ====> '), write(C1),
write('.... e ....'), write(C2),
nl,
fail.
ache_tudo.
