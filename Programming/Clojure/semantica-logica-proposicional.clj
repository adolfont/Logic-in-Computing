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

;; Testes
;; Lembrar de sempre usar o ":"
(defn testes-com-println-de-valora[]
  (println (valora :p '{:p 1}))
  (println (valora :p '{:p 0}))
  (println (valora [:p :or [:not :p]] '{:p 0}))
  (println (valora [:p :or [:not :p]] '{:p 1}))
  (println (valora [:p :and [:not :p]] '{:p 0}))
  (println (valora [:p :and [:not :p]] '{:p 1}))
  (println (valora [:p :and [:not :q]] '{:p 1, :q 0}))
  (println (valora [:p :or [:not :q]] '{:p 0, :q 1}))
  (println (valora [:r :implies [:p :or [:not :q]]] '{:p 0, :q 1, :r 1}))
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


;Testes
;(inteiro-para-binario 0 3)
;(inteiro-para-binario 0 2)
;(inteiro-para-binario 0 1)
;(inteiro-para-binario 0 0)
;(inteiro-para-binario 8 4)
;(inteiro-para-binario 8 5)


(defn cria-valoracao-binaria [quantidade-atomos]
    (loop [i 0 tresult '[]]
      (if (< i (int (Math/pow 2 quantidade-atomos)))
        (recur (+ i 1) (conj tresult (inteiro-para-binario i quantidade-atomos)))
        tresult
      )
    )
)

;(cria-valoracao-binaria 3)

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

;(lista-todas-as-valoracoes '[:p :q :r])

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


;(println (tabela-verdade '[:p :and :q] '[:p :q]))

;(println (tabela-verdade '[:p :implies [:p :and :q]] '[:p :q]))


;;;; SUBFORMULAS

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

; Testes

; (subformulas :p)
;#{:p}
; (subformulas [:not :p])
;#{[:not :p] :p}

;(subformulas [[:not :p] :and :q])
;#{[[:not :p] :and :q] :q [:not :p] :p}

;(subformulas [[:not :p] :and :p])
;#{[[:not :p] :and :p] [:not :p] :p}

(defn roda-testes-subformulas[]
   (println "Testes subformulas")
   (println
   (if
      (and
        (= (subformulas :p) #{:p})
        (= (subformulas [:not :p]) #{[:not :p] :p})
        (= (subformulas [[:not :p] :and :q]) #{[[:not :p] :and :q] :q [:not :p] :p})
        (= (subformulas [[:not :p] :and :p]) #{[[:not :p] :and :p] [:not :p] :p})
        )
      "Passou em todos!"
      "Falhou em ao menos um!"
    ))
)

(roda-testes-subformulas)



;;;;;;;;; TAMANHO

(defn tamanho [formula]
  (if (vector? formula) (count (flatten formula))
      1))

(defn roda-testes-tamanho[]
   (println "Testes tamanho")
   (println
   (if
      (and
        (= (tamanho :p) 1)
        (= (tamanho [:not :p]) 2)
        (= (tamanho [[:not :p] :and :q]) 4)
        (= (tamanho [[:not :p] :and [:not :q]]) 5)
        )
      "Passou em todos!"
      "Falhou em ao menos um!"
    ))
)

(roda-testes-tamanho)


;;;;;;;;; CLASSIFICACAO

(defn tabela-verdade-apenas-valores [formula atomos]
  (map second (tabela-verdade formula atomos)))

(defn filtro-atomos [elemento]
  (not (or (= elemento :and) (= elemento :or) (= elemento :not) (= elemento :implies))))

(defn atomos [formula]
  (set
  (if (vector? formula)
    (filter filtro-atomos (flatten formula))
    (vector formula))))



(defn satisfazivel? [formula]
  (= 1 (some #{1} (tabela-verdade-apenas-valores formula (atomos formula)) ))
)

(defn falsificavel? [formula]
  (= 0 (some #{0} (tabela-verdade-apenas-valores formula (atomos formula)) ))
)

(defn valida? [formula]
  (not (falsificavel? formula))
)

(defn insatisfazivel? [formula]
  (not (satisfazivel? formula))
)


(defn roda-testes-classificacoes[]
   (println "Testes classificacoes")
   (println
   (if
      (and
        (= (tabela-verdade-apenas-valores :p '[:p]) '(0 1))
        (= (filtro-atomos :and) false)
        (= (filtro-atomos :or) false)
        (= (filtro-atomos :implies) false)
        (= (filtro-atomos :not) false)
        (= (filtro-atomos :p) true)
        (= (atomos :p) #{:p})
        (= (atomos [:p :and :q]) #{:q :p})
        (= (atomos [[:p :implies [:p :or :r] ] :and [:not :q]]) #{:q :r :p})
        (= (satisfazivel? :p) true)
        (= (satisfazivel? [:p :implies :q]) true)
        (= (satisfazivel? [:p :and [:not :p]]) false)
        (= (falsificavel? :p) true)
        (= (falsificavel? [:p :implies :q]) true)
        (= (falsificavel? [:p :or [:not :p]]) false)
        (= (valida? [:p :implies :q]) false)
        (= (valida? [:p :and [:not :p]]) false)
        (= (valida? [:p :or [:not :p]]) true)
        (= (insatisfazivel? [:p :implies :q]) false)
        (= (insatisfazivel? [:p :and [:not :p]]) true)
        (= (insatisfazivel? [:p :or [:not :p]]) false)
        )
      "Passou em todos!"
      "Falhou em ao menos um!"
    ))
)

(roda-testes-classificacoes)


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


(defn roda-testes-consequencia-logica[]
   (println "Testes Consequencia Logica")
   (println
   (if
      (and
         (= (acrescenta-ands '[:p [:p :implies :q]]) '[:p :and [:p :implies :q]] )
         (= (acrescenta-ands '[:p [:p :implies :q] :r]) '[:p :and [[:p :implies :q] :and :r]] )
         (= (consequencia-logica? :p :p) true )
         (= (consequencia-logica? :p :q) false )
         (= (consequencia-logica? '[:p [:p :implies :q]] :q) true )
         (= (equivalencia-logica? :p :p) true )
        (= (equivalencia-logica? [:p :and :q] [:p :and [:q :and :p]]) true )
        )
      "Passou em todos!"
      "Falhou em ao menos um!"
    ))
)

(roda-testes-consequencia-logica)

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

(defn roda-testes-formula-bem-formada[]
   (println "Testes Formula Bem Formada")
   (println
   (if
      (and
        (= (formula-bem-formada? :p) true )
        (= (formula-bem-formada? [:p :q]) false )
        (= (formula-bem-formada? :not) false )
        (= (formula-bem-formada? [:not :p]) true )
        (= (formula-bem-formada? [:not :p :q]) false )
        (= (formula-bem-formada? [:not [:not :p]]) true )
        (= (formula-bem-formada? [:p :and :q]) true )
        (= (formula-bem-formada? [[:not :p] :and [:q :or :r]]) true )
        (= (formula-bem-formada? [[:bububu :p] :and [:q :or :r]]) false )
        )
      "Passou em todos!"
      "Falhou em ao menos um!"
    ))
)

(roda-testes-formula-bem-formada)
