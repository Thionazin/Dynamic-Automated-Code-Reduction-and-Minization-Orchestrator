/-
  Hyper-Gaussian activation (ai/hypergaussian/HypergaussianActivationFunction.java).
  With u = r² / 2σ², the activation is exp(-u^P); the platform uses order P = 2 and width σ = 0.5.
  Status: in superposition (never compiled).
-/
import Mathlib

namespace QuantumProof.Hypergaussian

/-- Every unit fires fully at its own center. -/
theorem peak_at_center : Real.exp (-((0 : ℝ) ^ 2)) = 1 := by
  simp

/-- Flat top: inside the shoulder (u ≤ 1), order 2 stays at least as high as a plain Gaussian. -/
theorem flatter_top (u : ℝ) (h0 : 0 ≤ u) (h1 : u ≤ 1) : Real.exp (-u) ≤ Real.exp (-(u ^ 2)) := by
  rw [Real.exp_le_exp]
  nlinarith

/-- Steep shoulders: beyond it (u ≥ 1), order 2 falls at least as fast as a plain Gaussian. -/
theorem steeper_shoulders (u : ℝ) (h1 : 1 ≤ u) : Real.exp (-(u ^ 2)) ≤ Real.exp (-u) := by
  rw [Real.exp_le_exp]
  nlinarith

/-- Between two one-hot features (r² = 2) at σ = 0.5, the exponent is exactly 16,
    so a unit fires at only e^-16 for any feature but its own. -/
theorem neighbor_exponent : ((2 : ℝ) / (2 * (1 / 2) ^ 2)) ^ 2 = 16 := by
  norm_num

end QuantumProof.Hypergaussian
