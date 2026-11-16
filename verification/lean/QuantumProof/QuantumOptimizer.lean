/-
  Quantum-Inspired Evolutionary Algorithm (quantum/QuantumInspiredOptimizer.java).
  Each measured candidate is repaired with the oracle (keep = oracle || measurement), and the best candidate is
  greedily polished (keep = best && oracle). Together with arbitration, the shipped decision is always the rule's.
  Status: in superposition (never compiled).
-/
import QuantumProof.Arbitration

namespace QuantumProof.QuantumOptimizer

open QuantumProof.Arbitration

/-- Measuring a Q-bit, then repairing it with the oracle. -/
def repair (oracle measured : Bool) : Bool := oracle || measured

/-- Greedy local search: drop every kept space that the oracle does not need. -/
def polish (best oracle : Bool) : Bool := best && oracle

/-- Repair never drops a space the oracle requires. -/
theorem repair_keeps_oracle (oracle measured : Bool) (h : oracle = true) : repair oracle measured = true := by
  subst h; rfl

/-- Any candidate that keeps every required space polishes down to exactly the oracle. -/
theorem polish_of_superset (best oracle : Bool) (h : oracle = true → best = true) : polish best oracle = oracle := by
  cases best <;> cases oracle <;> simp_all [polish]

/-- Whatever the Q-bits measure, a repaired-then-polished candidate is exactly the oracle. -/
theorem polish_repair (oracle measured : Bool) : polish (repair oracle measured) oracle = oracle := by
  cases oracle <;> cases measured <;> rfl

/-- End to end: neural network, arbitration and quantum optimizer together ship exactly the safety rule's answer,
    for every learned decision and every quantum measurement. -/
theorem shipped_decision_is_rule (learned rule measured : Bool) :
    polish (repair (arbitrate learned rule) measured) (arbitrate learned rule) = rule := by
  rw [arbitrate_eq_rule]
  exact polish_repair rule measured

end QuantumProof.QuantumOptimizer
