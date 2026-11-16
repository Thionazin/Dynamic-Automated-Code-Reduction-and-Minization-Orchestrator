/-
  The neural network (ai/model/SpacePerceptron.java): a sigmoid over the weighted features.
  Status: in superposition (never compiled).
-/
import Mathlib

namespace QuantumProof.Perceptron

/-- The perceptron's output. -/
noncomputable def sigmoid (s : ℝ) : ℝ := 1 / (1 + Real.exp (-s))

/-- The neural network always outputs a probability strictly between 0 and 1, so it is never certain of anything. -/
theorem sigmoid_mem_unit_interval (s : ℝ) : 0 < sigmoid s ∧ sigmoid s < 1 := by
  unfold sigmoid
  have he := Real.exp_pos (-s)
  exact ⟨by positivity, (div_lt_one (by positivity)).mpr (by linarith)⟩

/-- An untrained (all-zero) network is perfectly undecided. -/
theorem untrained_is_undecided : sigmoid 0 = 1 / 2 := by
  unfold sigmoid
  norm_num

end QuantumProof.Perceptron
