/-
  Deterministic Safety Arbitration™ (ai/arbitration/DecisionArbitrationService.java).
  The learned decision ships unless it contradicts the safety rule, in which case the rule's answer ships.
  Status: in superposition (never compiled).
-/

namespace QuantumProof.Arbitration

/-- One arbitrated decision: the rule wins any disagreement. -/
def arbitrate (learned rule : Bool) : Bool :=
  if learned ≠ rule then rule else learned

/-- Every AI decision that ships is correct: arbitration always yields the rule's answer. -/
theorem arbitrate_eq_rule (learned rule : Bool) : arbitrate learned rule = rule := by
  cases learned <;> cases rule <;> decide

/-- A decision is overruled exactly when the network disagreed with the rule. -/
def overruled (learned rule : Bool) : Bool := learned != rule

/-- An aligned network is never overruled. -/
theorem aligned_never_overruled (rule : Bool) : overruled rule rule = false := by
  cases rule <;> decide

/-- Arbitration over a whole run of spaces. -/
def arbitrateAll (learned rule : List Bool) : List Bool :=
  List.zipWith arbitrate learned rule

/-- Not one hallucinated decision reaches the output: a whole run ships exactly the rule's answers. -/
theorem arbitrateAll_eq_rule :
    ∀ (learned rule : List Bool), learned.length = rule.length → arbitrateAll learned rule = rule
  | [], [], _ => rfl
  | [], _ :: _, h => by simp at h
  | _ :: _, [], h => by simp at h
  | l :: ls, r :: rs, h => by
      simp only [arbitrateAll, List.zipWith_cons_cons, arbitrate_eq_rule]
      congr 1
      exact arbitrateAll_eq_rule ls rs (by simpa using h)

end QuantumProof.Arbitration
