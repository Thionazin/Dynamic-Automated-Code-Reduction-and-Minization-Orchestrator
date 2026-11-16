/-
  One-hot feature encoding (ai/model/FeatureExtractor.java).
  The (previous class, next class) pair, with 3 classes each, maps to index prev * 3 + next among 9 features.
  Status: in superposition (never compiled).
-/

namespace QuantumProof.FeatureEncoding

/-- The index of the single active feature. -/
def featureIndex (prev next : Fin 3) : Nat := prev.val * 3 + next.val

/-- Every pair lands inside the 9-feature input. -/
theorem featureIndex_lt_nine : ∀ prev next : Fin 3, featureIndex prev next < 9 := by
  decide

/-- Distinct class pairs never share a feature, so the encoding loses nothing. -/
theorem featureIndex_injective :
    ∀ p n p' n' : Fin 3, featureIndex p n = featureIndex p' n' → p = p' ∧ n = n' := by
  decide

end QuantumProof.FeatureEncoding
