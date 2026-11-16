/-
  Q-bit readout (quantum/QuantumInspiredOptimizer.java, ai/hypergaussian/QuantumInspiredHypergaussianNetwork.java).
  A Q-bit with angle θ is kept with probability sin²θ (the Born rule); priors enter as θ = arcsin √p; rotations clamp
  θ to [0, π/2].
  Status: in superposition (never compiled).
-/
import Mathlib

namespace QuantumProof.BornRule

/-- The Born rule always yields a valid probability. -/
theorem born_probability (θ : ℝ) : 0 ≤ Real.sin θ ^ 2 ∧ Real.sin θ ^ 2 ≤ 1 :=
  ⟨sq_nonneg _, Real.sin_sq_le_one θ⟩

/-- An untrained Q-bit at π/4 is in uniform superposition: exactly one half. -/
theorem uniform_superposition : Real.sin (Real.pi / 4) ^ 2 = 1 / 2 := by
  rw [Real.sin_pi_div_four, div_pow, Real.sq_sqrt (by norm_num : (0 : ℝ) ≤ 2)]
  norm_num

/-- Encoding a prior as θ = arcsin √p and measuring it gives back exactly p. -/
theorem prior_roundtrip (p : ℝ) (hp0 : 0 ≤ p) (hp1 : p ≤ 1) :
    Real.sin (Real.arcsin (Real.sqrt p)) ^ 2 = p := by
  have hs0 : 0 ≤ Real.sqrt p := Real.sqrt_nonneg p
  have hs1 : Real.sqrt p ≤ 1 :=
    calc Real.sqrt p ≤ Real.sqrt 1 := Real.sqrt_le_sqrt hp1
      _ = 1 := Real.sqrt_one
  rw [Real.sin_arcsin (by linarith) hs1, Real.sq_sqrt hp0]

/-- The rotation gate's clamp keeps every angle inside [0, π/2]. -/
theorem rotation_clamped (t : ℝ) :
    0 ≤ max 0 (min (Real.pi / 2) t) ∧ max 0 (min (Real.pi / 2) t) ≤ Real.pi / 2 :=
  ⟨le_max_left _ _, max_le (by positivity) (min_le_left _ _)⟩

end QuantumProof.BornRule
