# Engineering Dossier

This document is the compact technical map behind the public repositories on [`fr3kchy`](https://github.com/fr3kchy).

## Position

Michael Insch (`fr3k`) is an independent systems programmer and AI infrastructure operator based in North Queensland. The strongest work sits at system boundaries: model ↔ tool, agent ↔ host, control plane ↔ transport, software ↔ radio, and autonomy ↔ human authority.

The portfolio is intentionally evidence-oriented. Public repositories expose runnable code, tests, raw benchmark results, acceptance matrices and known limitations rather than only product descriptions.

## Proven systems

### 1. Resilient AI-assisted communications

**Repository:** [`hermes-reticulum-platform`](https://github.com/fr3kchy/hermes-reticulum-platform)

The system treats Reticulum/LXMF as sovereign transport and Hermes as an optional policy endpoint. The gateway does not own `rnsd`; failure of the model, database, broker, monitoring stack, Internet connection or gateway must not stop signed human LXMF traffic.

Implemented controls include:

- persistent atomic identities and corrupt-identity hard failure
- strict signature, hash and message-ID validation
- immutable inbound envelopes and durable replay protection
- bounded callbacks, worker queues, concurrency, rate and reply bytes
- local-first inference and explicit cloud-egress approval
- a restricted no-tool Hermes profile tested through attempted side effects
- real sender/receiver processes and signed LXMF delivery
- independent hardened systemd units and recovery drills

**Evidence:** 38 tests, static/security gates, package build, virtual/LAN delivery, gateway E2E and restart persistence all passed. Physical RF remains explicitly not tested.

### 2. Distributed Hermes control plane

**Repository:** [`hermes-fleet-control`](https://github.com/fr3kchy/hermes-fleet-control)

A FastAPI/SQLite control plane with single-use enrolment, HMAC-signed heartbeats, durable idempotent tasks, bounded workers, event replay, WebSocket streaming, metrics and systemd deployment.

The design does not pretend queueing equals execution. A `202` response means durable admission only; worker terminal state is independently recorded and observed.

**Evidence:** 16 tests covering security, migrations, adapters, service definitions and orchestration.

### 3. FR3K community infrastructure programme

**Repository:** [`fr3k-infrastructure`](https://github.com/fr3kchy/fr3k-infrastructure)

FR3K is a North Queensland programme for experimental, supplementary community communications. The repository converts strategy documents into executable governance controls.

The initial implementation pack passed every supplied SHA-256 check but failed operational acceptance because prompt counts disagreed, SOP bodies were missing, task/risk data was malformed and named P0 owners/sign-offs were absent. The correct result was a machine-readable **BLOCKED** G0 gate—not a ceremonial launch.

### 4. Constrained-hardware model optimisation

**Repository:** [`qwen36-bench`](https://github.com/fr3kchy/qwen36-bench)

A reproducible Ollama sweep for a 35B-A3B mixture-of-experts model on a 4 GB Quadro T1000. The winning configuration reached 5.71 generated tokens/s with 3/3 short-suite checks passing. The repository retains configuration, harness, raw JSON and report.

### 5. Agent evaluation and economics

Public demonstrations cover:

- golden-dataset evaluation and drift detection
- trajectory capture and replay across model/prompt variants
- cost-aware model routing
- hosted pgvector/MMR RAG and fully local FAISS/Ollama RAG
- GGUF quantisation sweeps across size, speed and quality

These are small, inspectable systems rather than screenshots of larger private stacks.

## Active private engineering

The public repositories are the review surface, not the whole operating environment. Active private systems include:

- Hermes/OpenClaw/Paperclip orchestration and governance
- Bybit trading execution, signal and risk infrastructure
- 601-notebook Colab research vault and semantic discovery layer
- ComfyUI/FLUX/Wan/FramePack multimodal pipelines
- Reachy Mini embodiment with speech, vision, action and learning loops
- multi-host GPU routing and model inventory management
- agent memory, skill evolution, observability and self-improvement systems

Private status is retained where credentials, customer/job material, trading logic, explicit media, device identities or operational security make public release irresponsible.

## Operating scale — observed on 2 August 2026

- 48 local Git repositories under active host management
- 33 GitHub repositories before this portfolio expansion
- 88 Ollama models available on the primary host
- NVIDIA RTX 3060 12 GB local inference/generation GPU
- active Hermes gateway, Paperclip server and Paperclip gateway services
- Linux-first deployment with systemd, tmux, Docker, Python, TypeScript/JavaScript, SQLite/PostgreSQL and GPU tooling

## Public-source research discipline

For the Reticulum platform, the research phase classified hundreds of public repositories and related artefacts for architectural relevance, provenance and licence compatibility. Patterns were adopted only when they survived reliability and licensing gates. Unlicensed, GPL and AGPL sources were treated as pattern-only unless the target licence explicitly permitted incorporation.

A substantial public-source review of the LizTheDeveloper ecosystem informed failure-domain, durable-envelope, bounded-execution, discovery and static-authority comparisons. The outcome was deliberately conservative: no repository was directly copied or adopted merely because it was interesting.

## Collaboration signal

The value is not familiarity with one framework. It is the ability to move from ambiguous intent to:

1. ground-truth inspection;
2. explicit architecture and failure domains;
3. implementation with bounded authority;
4. real execution and measured evidence;
5. honest acceptance or blocked-state reporting;
6. operational handover, recovery and iteration.

That is the case this GitHub profile is designed to make.
