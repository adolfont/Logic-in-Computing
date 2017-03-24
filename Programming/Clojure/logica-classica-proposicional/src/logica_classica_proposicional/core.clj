(ns logica-classica-proposicional.core
  (:gen-class))

(declare cria-valoracao-binaria)

(defn -main
  "Sintaxe e Semântica de Lógica Clássica Proposicional"
  [& args]
  (println "Sintaxe e Semântica de Lógica Clássica Proposicional")
)


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
  (let [conectivo (second formula)
        esquerda (first formula)
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


;;; Funções para tabelas-verdade

(defn inteiro-para-binario
  "converte de inteiro para binário (como vetor) levando em consideração o número de dígitos.
  Exemplos: (inteiro-para-binario 0 2) = '(0 0)
  (inteiro-para-binario 1 3) = '(0 0 1)"
 [valor qtde-digitos]
  (loop [digito 0 valor-em-processamento valor result '[]]
    (if (or (< digito qtde-digitos) (> valor-em-processamento 0))
     (recur (inc digito)
            (quot valor-em-processamento 2)
            (into [(rem valor-em-processamento 2)] result ) )
     result
    ))
)


(defn cria-valoracao-binaria [quantidade-atomos]
    (loop [i 0 tresult '[]]
      (if (< i (int (Math/pow 2 quantidade-atomos)))
        (recur (+ i 1) (conj tresult (inteiro-para-binario i quantidade-atomos)))
        tresult
      )
    )
)

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

(declare atomos)

(defn tabela-verdade [formula]
  (let [todas-as-valoracoes (lista-todas-as-valoracoes (atomos formula))
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

(defn tabela-verdade-apenas-valores [formula]
  (map second (tabela-verdade formula)))


(defn filtro-atomos [elemento]
  (not (or (= elemento :and) (= elemento :or) (= elemento :not) (= elemento :implies))))


(defn atomos [formula]
  (set
  (if (vector? formula)
    (filter filtro-atomos (flatten formula))
    (vector formula))))


;;; SUBFORMULAS

(defn subformulas-aux [formula result]
  (if (vector? formula)
      (if (= :not (first formula))
          (subformulas-aux (second formula)
                (conj result formula))
          (subformulas-aux (first formula)
                       (subformulas-aux (nth formula 2)
                      (conj result formula)))
      )
      (conj result formula)
  )
)

(defn subformulas [formula]
  (set (subformulas-aux formula '[])))


;;;;;;;;; TAMANHO

(defn tamanho [formula]
  (if (vector? formula) (count (flatten formula))
      1))


;;;;;;;;;; classificacoes

(defn satisfazivel? [formula]
  (= 1 (some #{1} (tabela-verdade-apenas-valores formula) ))
)

(defn falsificavel? [formula]
  (= 0 (some #{0} (tabela-verdade-apenas-valores formula) ))
)

(defn valida? [formula]
  (not (falsificavel? formula))
)

(defn insatisfazivel? [formula]
  (not (satisfazivel? formula))
)


;; CONSEQUENCIA LOGICA

(defn acrescenta-ands [premissas]
  (if (= 1 (count premissas)) (first premissas)
    (if (= 2 (count premissas))
        [(first premissas) :and (second premissas)]
        [(first premissas) :and (acrescenta-ands (rest premissas))]
        )))

(defn consequencia-logica? [premissas conclusao]
  (if (not (vector? premissas))
      (valida? [  [:not premissas]                    :or conclusao] )
      (valida? [  [:not (acrescenta-ands premissas)]  :or conclusao] )
  )
)

(defn equivalencia-logica? [esquerda direita]
      (valida? [[ [:not esquerda] :or direita] :and [ [:not direita] :or esquerda] ] )
)


;;; FORMULA BEM FORMADA

(defn formula-bem-formada? [formula])

(defn formula-bem-formada-com-not? [formula]
  (and (vector? formula) (= :not (first formula)) (= (count formula) 2)
       (formula-bem-formada? (second formula)))
)

(defn formula-bem-formada-com-conectivo-binario? [formula]
  (if (and (vector? formula) (some (hash-set (second formula)) '(:and :or :implies))
        (= (count formula) 3)
       (formula-bem-formada? (first formula))
       (formula-bem-formada? (nth formula 2))
       )
       true
       false)
)

(defn formula-bem-formada? [formula]
  (if (some #{formula} '(:not :and :or :implies))
      false
      (if (or (keyword? formula)
          (formula-bem-formada-com-not? formula)
          (formula-bem-formada-com-conectivo-binario? formula)
          )
          true
          false)
  )
)

; Conversões de conectivos
; NAND
; NOR
; BI-IMPLIES
; XOR

(defn converte-conectivos [formula]
  (if (vector? formula)
     (condp = (second formula)
       :not-and [:not [(first formula) :and (nth formula 2) ]  ]
       :not-or [:not [(first formula) :or (nth formula 2) ]  ]
       :bi-implies [[(first formula) :implies (nth formula 2) ] :and
                   [(nth formula 2) :implies (first formula) ]]
       :xor [[(first formula) :or (nth formula 2) ] :and
                   [:not [(first formula) :and (nth formula 2) ]]]
       [(converte-conectivos (first formula)) (second formula) (converte-conectivos(nth formula 2)) ]
     )
     formula
 )
)
