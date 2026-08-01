# Reviewer Guide

## 15-minute route

1. [`hermes-reticulum-platform/docs/FINAL_ACCEPTANCE.md`](https://github.com/fr3kchy/hermes-reticulum-platform/blob/main/docs/FINAL_ACCEPTANCE.md)
2. [`hermes-reticulum-platform/src/hermes_lxmf_gateway`](https://github.com/fr3kchy/hermes-reticulum-platform/tree/main/src/hermes_lxmf_gateway)
3. [`hermes-fleet-control/src/hermes_fleet`](https://github.com/fr3kchy/hermes-fleet-control/tree/main/src/hermes_fleet)
4. [`fr3k-infrastructure/evidence/g0-result.json`](https://github.com/fr3kchy/fr3k-infrastructure/blob/main/evidence/g0-result.json)
5. [`qwen36-bench/results/REPORT_workstation.md`](https://github.com/fr3kchy/qwen36-bench/blob/main/results/REPORT_workstation.md)

## Questions the portfolio answers

- Can he separate transport from optional AI control? **Yes — implemented and failure-tested.**
- Can he build durable, authenticated orchestration? **Yes — fleet control has durable queues, signed heartbeats and replayable events.**
- Can he reject bad source material instead of polishing it? **Yes — FR3K G0 remains blocked with machine-readable reasons.**
- Can he optimise models on poor hardware using measurement rather than lore? **Yes — raw constrained-GPU sweep results are included.**
- Can he state limitations? **Every flagship has an explicit acceptance boundary.**

## Suggested deeper review

- Identity durability and address correctness
- Replay/audit fail-closed behaviour
- bounded queue/rate/concurrency controls
- service independence and recovery documents
- benchmark methodology and raw result consistency
- distinction between observed, test-confirmed and not-tested claims
