(ns logica-classica-proposicional.core-test
  (:require [clojure.test :refer :all]
            [logica-classica-proposicional.core :refer :all]))

(deftest operadores-logicos-test
  (testing "Operador And."
    (is (= (land 1 1) 1))
    (is (= (land 0 1) 0))
    (is (= (land 1 0) 0))
    (is (= (land 0 0) 0)))


  (testing "Operador Or."
    (is (= (lor 1 1) 1))
    (is (= (lor 0 1) 1))
    (is (= (lor 1 0) 1))
    (is (= (lor 0 0) 0)))


  (testing "Operador Not."
    (is (= (lnot 1) 0))
    (is (= (lnot 0) 1))))


(deftest valoracoes-test
  (testing "Valorações"
    (is (=  (valora :p '{:p 1}) 1))
    (is (=  (valora :p '{:p 0}) 0))
    (is (=   (valora [:p :or [:not :p]] '{:p 0})                  1 ))
    (is (=   (valora [:p :or [:not :p]] '{:p 1})                  1 ))
    (is (=    (valora [:p :and [:not :p]] '{:p 0})                 0 ))
    (is (=  (valora [:p :and [:not :p]] '{:p 1})                  0  ))
    (is (=  (valora [:p :and [:not :q]] '{:p 1, :q 0})             1       ))
    (is (=  (valora [:p :or [:not :q]] '{:p 0, :q 1})               0     ))
    (is (=   (valora [:r :implies [:p :or [:not :q]]] '{:p 0, :q 1, :r 1})     0              ))
)
)

(deftest tabela-verdade-test
  (testing "Funções relacionadas a tabelas-verdade: inteiro-para-binario"
    (is (= (inteiro-para-binario 0 3) '(0 0 0)))
    (is (= (inteiro-para-binario 8 4) '(1 0 0 0)))
  )

  (testing "Cria valoração binária"
   (is (= (cria-valoracao-binaria 1)
        '[[0] [1]]))
   (is (= (cria-valoracao-binaria 2)
        '[[0 0] [0 1] [1 0] [1 1]]))
   (is (= (cria-valoracao-binaria 3)
        '[[0 0 0] [0 0 1] [0 1 0] [0 1 1] [1 0 0] [1 0 1] [1 1 0] [1 1 1]]))
  )

  (testing "Lista todas as valorações"
   (is (=  (lista-todas-as-valoracoes '[:p])
           '({:p 0} {:p 1})))
   (is (= (lista-todas-as-valoracoes '[:p :q])
           '({:p 0, :q 0} {:p 0, :q 1} {:p 1, :q 0} {:p 1, :q 1})))
   (is (=  (lista-todas-as-valoracoes '[:p :q :r])
         '({:p 0, :q 0, :r 0} {:p 0, :q 0, :r 1} {:p 0, :q 1, :r 0} {:p 0, :q 1, :r 1} {:p 1, :q 0, :r 0} {:p 1, :q 0, :r 1} {:p 1, :q 1, :r 0} {:p 1, :q 1, :r 1})))
 )

  (testing "Função tabela-verdade principal"
   (is (= (tabela-verdade '[:p :and :q]) '[[{:q 0, :p 0} 0] [{:q 0, :p 1} 0] [{:q 1, :p 0} 0] [{:q 1, :p 1} 1]]  ))
   (is (= (tabela-verdade '[[:not :p] :or :q]) '[[{:q 0, :p 0} 1] [{:q 0, :p 1} 0] [{:q 1, :p 0} 1] [{:q 1, :p 1} 1]]  ))
  )
  (testing "Função tabela-verdade apenas valores"
    (is    (= (tabela-verdade-apenas-valores :p) '(0 1)))
    (is (= (tabela-verdade-apenas-valores '[:p :and :q]) '[0  0 0  1]  ))
  )
)

(deftest subformula-test
  (testing "Cálculo de subfórmulas"
    (is (= (subformulas :p) #{:p}))
    (is (= (subformulas [:not :p]) #{[:not :p] :p}))
    (is (= (subformulas [[:not :p] :and :q]) #{[[:not :p] :and :q] :q [:not :p] :p}))
    (is (= (subformulas [[:not :p] :and :p]) #{[[:not :p] :and :p] [:not :p] :p}))
  )
 )

(deftest tamanho-test
   (testing "Função tamanho"
    (is   (= (tamanho :p) 1))
    (is   (= (tamanho [:not :p]) 2))
    (is   (= (tamanho [[:not :p] :and :q]) 4))
    (is   (= (tamanho [[:not :p] :and [:not :q]]) 5))))


(deftest classificacoes-test
   (testing "Testes classificacoes"
    (is    (= (filtro-atomos :and) false))
    (is    (= (filtro-atomos :or) false))
    (is    (= (filtro-atomos :implies) false))
    (is    (= (filtro-atomos :not) false))
    (is    (= (filtro-atomos :p) true))
    (is    (= (atomos :p) #{:p}))
    (is    (= (atomos [:p :and :q]) #{:q :p}))
    (is    (= (atomos [[:p :implies [:p :or :r] ] :and [:not :q]]) #{:q :r :p}))
    (is    (= (satisfazivel? :p) true))
    (is    (= (satisfazivel? [:p :implies :q]) true))
    (is    (= (satisfazivel? [:p :and [:not :p]]) false))
    (is    (= (falsificavel? :p) true))
    (is    (= (falsificavel? [:p :implies :q]) true))
    (is    (= (falsificavel? [:p :or [:not :p]]) false))
    (is    (= (valida? [:p :implies :q]) false))
    (is    (= (valida? [:p :and [:not :p]]) false))
    (is    (= (valida? [:p :or [:not :p]]) true))
    (is    (= (insatisfazivel? [:p :implies :q]) false))
    (is    (= (insatisfazivel? [:p :and [:not :p]]) true))
    (is    (= (insatisfazivel? [:p :or [:not :p]]) false))
  )
)

(deftest consequencia-logica-test
   (testing "Testes Consequencia Logica"
    (is     (= (acrescenta-ands '[:p [:p :implies :q]]) '[:p :and [:p :implies :q]] ))
    (is     (= (acrescenta-ands '[:p [:p :implies :q] :r]) '[:p :and [[:p :implies :q] :and :r]] ))
    (is     (= (consequencia-logica? :p :p) true ))
    (is     (= (consequencia-logica? :p :q) false ))
    (is     (= (consequencia-logica? '[:p [:p :implies :q]] :q) true ))
    (is     (= (equivalencia-logica? :p :p) true ))
    (is    (= (equivalencia-logica? [:p :and :q] [:p :and [:q :and :p]]) true ))))


(deftest formula-bem-formada-test
   (testing "Testes Formula Bem Formada")
      (is        (= (formula-bem-formada? :p) true ))
      (is  (= (formula-bem-formada? [:p :q]) false ))
      (is  (= (formula-bem-formada? :not) false ))
      (is  (= (formula-bem-formada? [:not :p]) true ))
      (is  (= (formula-bem-formada? [:not :p :q]) false ))
      (is  (= (formula-bem-formada? [:not [:not :p]]) true ))
      (is  (= (formula-bem-formada? [:p :and :q]) true ))
      (is  (= (formula-bem-formada? [[:not :p] :and [:q :or :r]]) true ))
      (is  (= (formula-bem-formada? [[:bububu :p] :and [:q :or :r]]) false )))

(deftest conversao-conectivos-test
  (testing "Nand (não-e), Nor (não-ou), Bi-implies (bi-implicação), Xor (ou exclusivo)"
    (is (= (converte-conectivos '[:p :not-and :q]) '[:not [:p :and :q]] ))
    (is (= (converte-conectivos '[:p :not-or :q]) '[:not [:p :or :q]] ))
    (is (= (converte-conectivos '[:p :bi-implies :q]) '[[:p :implies :q] :and [:q :implies :p]] ))
    (is (= (converte-conectivos '[:p :xor :q]) '[[:p :or :q] :and [:not [:p :and :q]]] ))
    (is (= (converte-conectivos '[:p :and :q]) '[:p :and :q] ))
    (is (= (converte-conectivos '[:p :and [:q :xor :r]]) '[:p :and [[:q :or :r] :and [:not [:q :and :r]]]] ))
    (is (= (tabela-verdade-apenas-valores (converte-conectivos '[:p :xor :q]) ) '(0 1 1 0)))
  )
)
