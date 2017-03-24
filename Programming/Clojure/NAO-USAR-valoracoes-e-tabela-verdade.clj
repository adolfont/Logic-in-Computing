;; Versão anterior - NÃO USAR

;; Operadores Lógicos
(defn land [lvalue-1 lvalue-2]
   (* lvalue-1 lvalue-2)
  )
(defn lor [lvalue-1 lvalue-2]
   (max lvalue-1 lvalue-2)
)
(defn lnot [lvalue]
  (- 1 lvalue))

;; apenas para poder usar recursivamente
(defn valora [formula valoracao]
)

;; função auxiliar para e, ou e implicação
(defn valora-conectivos-binarios [formula valoracao]
  (let [conectivo (first formula)
        esquerda (second formula)
        direita (nth  formula 2)]
    (condp = conectivo
        :and  (land (valora esquerda valoracao) (valora direita valoracao))
        :or   (lor (valora esquerda valoracao) (valora direita valoracao))
        :implies (lor  (lnot (valora esquerda valoracao)) (valora direita valoracao))
      )
  )
)

;; função de valoração
(defn valora [formula valoracao]
   (if (not (vector? formula))
       (formula valoracao)
       (if (= :not (first formula))
           (lnot (valora (second formula) valoracao))
           (valora-conectivos-binarios formula valoracao))
   )
)


(defn inteiro-para-binario [valor qtde-digitos]
  (loop [digito 0 valor-em-processamento valor result '[]]
    (if (or (< digito qtde-digitos) (> valor-em-processamento 0))
     (recur (inc digito)
            (quot valor-em-processamento 2)
            (into [(rem valor-em-processamento 2)] result ) )
     result
    ))
)

(inteiro-para-binario 0 3)
(inteiro-para-binario 0 2)
(inteiro-para-binario 0 1)
(inteiro-para-binario 0 0)
(inteiro-para-binario 8 4)
(inteiro-para-binario 8 5)


(defn cria-valoracao-binaria [quantidade-atomos]
    (loop [i 0 tresult '[]]
      (if (< i (int (Math/pow 2 quantidade-atomos)))
        (recur (+ i 1) (conj tresult (inteiro-para-binario i quantidade-atomos)))
        tresult
      )
    )
)

(cria-valoracao-binaria 3)

(defn my-zipmap [keys values]
   (into {} (map vector  keys values))
)

(defn lista-todas-as-valoracoes [atomos]
  (let [valoracoes (cria-valoracao-binaria (count atomos) )
        total-valoracoes (count valoracoes)]
    (loop [i 0 result '[]]
      (if (< i total-valoracoes)
          (recur (+ i 1) (into [(my-zipmap atomos (nth valoracoes i))] result ))
           (reverse result)
      )
    )
  )
)

(lista-todas-as-valoracoes '[:p :q :r])

(defn tabela-verdade [formula atomos]
  (let [todas-as-valoracoes (lista-todas-as-valoracoes atomos)
        total-valoracoes (count todas-as-valoracoes)]
    (loop [i 0 result '[]]
       (if (< i total-valoracoes)
           (recur (+ i 1)
                  (conj result [(nth todas-as-valoracoes i)
                    (valora formula (nth todas-as-valoracoes i))])
           )
           result
      )
    )
  )
)


(println (tabela-verdade '[:and :p :q] '[:p :q]))
