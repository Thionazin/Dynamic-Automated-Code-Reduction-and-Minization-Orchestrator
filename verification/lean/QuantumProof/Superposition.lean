/-
  Superposition ensemble (ai/ensemble/SuperpositionEnsemble.java).
  Two models with keep-probabilities p and q combine as ((√p + √q) / 2)²: amplitudes average, then square.
  Status: in superposition (never compiled).
-/
import Mathlib

namespace QuantumProof.Superposition

/-- The ensemble's output. -/
noncomputable def ensemble (p q : ℝ) : ℝ := ((Real.sqrt p + Real.sqrt q) / 2) ^ 2

/-- Interference never exceeds the classical average (the gap is ((√p - √q) / 2)²). -/
theorem interference_le_classical (p q : ℝ) (hp : 0 ≤ p) (hq : 0 ≤ q) : ensemble p q ≤ (p + q) / 2 := by
  unfold ensemble
  have ha := Real.sq_sqrt hp
  have hb := Real.sq_sqrt hq
  nlinarith [sq_nonneg (Real.sqrt p - Real.sqrt q)]

/-- Two valid probabilities always combine into a valid probability. -/
theorem ensemble_is_probability (p q : ℝ) (hp0 : 0 ≤ p) (hp1 : p ≤ 1) (hq0 : 0 ≤ q) (hq1 : q ≤ 1) :
    0 ≤ ensemble p q ∧ ensemble p q ≤ 1 := by
  refine ⟨by unfold ensemble; positivity, ?_⟩
  have := interference_le_classical p q hp0 hq0
  linarith

/-- Agreement is preserved: two models that agree on p yield p. -/
theorem constructive_interference (p : ℝ) (hp : 0 ≤ p) : ensemble p p = p := by
  unfold ensemble
  rw [← two_mul, mul_div_cancel_left₀ _ (two_ne_zero), Real.sq_sqrt hp]

/-- Total disagreement interferes destructively: certainty against impossibility gives 1/4, not the classical 1/2. -/
theorem destructive_interference : ensemble 1 0 = 1 / 4 := by
  unfold ensemble
  norm_num [Real.sqrt_one, Real.sqrt_zero]

end QuantumProof.Superposition
