# Michael Insch — `fr3k`

**Independent AI systems architect · programmer · operator**  
North Queensland, Australia

I build autonomous systems where **models, tools, infrastructure, hardware, safety and operations have to agree**.

My work spans local-first agent infrastructure, resilient communications, distributed control planes, model evaluation and optimisation, RAG, multimodal generation, embodied AI and evidence-gated field systems. I do not count scaffolds as finished systems and I do not report simulated passes as execution evidence.

## Current engineering surface

- **Sovereign agent infrastructure:** Hermes Agent, OpenClaw and Paperclip systems running as independently supervised services.
- **Resilient communications:** Reticulum/LXMF transport with a fail-closed optional Hermes policy gateway.
- **Distributed agent operations:** authenticated fleet enrolment, durable task dispatch, replayable events and bounded workers.
- **FR3K infrastructure:** community-owned communications research for North Queensland, controlled by explicit regulatory, safety and evidence gates.
- **Local model engineering:** 88-model Ollama inventory, quantisation, context/VRAM sweeps and task-specific evaluation across an RTX 3060 and constrained GPUs.
- **Embodied and multimodal AI:** Reachy Mini physical I/O, speech/vision/action routing, ComfyUI, FLUX, Wan and local video pipelines.
- **Agent evaluation:** golden datasets, drift detection, trajectory replay, cost-aware routing and local/cloud comparative harnesses.

## Flagship systems

| Repository | What it demonstrates | Executed evidence |
|---|---|---|
| [hermes-reticulum-platform](https://github.com/fr3kchy/hermes-reticulum-platform) | Independent Reticulum/LXMF transport plus a bounded Hermes policy gateway | 38 tests; signed virtual delivery; gateway E2E; restart persistence; transport survives gateway failure |
| [hermes-fleet-control](https://github.com/fr3kchy/hermes-fleet-control) | Secure local-first control plane for distributed Hermes nodes | 16 tests; HMAC heartbeats; durable task queue; authenticated events and metrics |
| [fr3k-infrastructure](https://github.com/fr3kchy/fr3k-infrastructure) | Executable governance and evidence gates for community communications infrastructure | Pack checksum verification; automated G0 decision gate; explicit blocked-state handling |
| [qwen36-bench](https://github.com/fr3kchy/qwen36-bench) | Reproducible Ollama optimisation on constrained hardware | 35B MoE model on 4 GB VRAM; 5.71 tok/s peak; all short-suite checks passed |
| [trajectory-replay-demo](https://github.com/fr3kchy/trajectory-replay-demo) | Record and replay agent tool trajectories for model/prompt A/B testing | SQLite-backed replay and comparative evaluation |
| [agent-cost-router-demo](https://github.com/fr3kchy/agent-cost-router-demo) | Route tasks to the cheapest viable model instead of defaulting to frontier inference | Measured cost-routing baseline |
| [mcp-rag-pgvector-demo](https://github.com/fr3kchy/mcp-rag-pgvector-demo) | Production-shaped RAG with pgvector, embeddings, MMR and evaluation | Reproducible pipeline and evaluation harness |
| [ollama-local-rag-demo](https://github.com/fr3kchy/ollama-local-rag-demo) | Fully local RAG with Ollama and FAISS | Zero-cloud retrieval and generation path |
| [agent-eval-harness-demo](https://github.com/fr3kchy/agent-eval-harness-demo) | Golden-dataset agent evaluation and drift detection | 25-question baseline with local and hosted backends |
| [divine-arsenal](https://github.com/fr3kchy/divine-arsenal) | Domain-specific computation joining ephemeris data, planetary hours and operational rules | Deterministic Python implementation rather than symbolic prose |
| [openclaw-workshop](https://github.com/fr3kchy/openclaw-workshop) | Two-day hands-on curriculum for agents, MCP and AI coding tools | Structured labs and deployment material |

## How I engineer

1. **Operational truth outranks mythology.** Read the running system, not just its design document.
2. **Transport and control are separate failure domains.** Optional AI must not weaken the underlying system.
3. **Evidence is graded.** Observation, unit tests, integration tests and live acceptance are not interchangeable.
4. **Security boundaries are explicit.** Inputs are untrusted, queues are bounded, privileged audit fails closed and cloud egress is gated.
5. **Hardware claims require hardware.** Virtual/LAN acceptance is never relabelled as RF, robotics or field acceptance.
6. **Useful failure reports beat polished fiction.** A blocked gate is a valid engineering result.

## What I am building now

- governed FR3K bench and field architecture for Magnetic Island and North Queensland
- Reticulum/LXMF identity, routing and offline messaging systems
- a distributed Hermes fleet with durable orchestration and independent node operation
- local multimodal and embodied-agent pipelines with measurable latency, memory and reliability constraints
- benchmark-gated self-improvement loops for agents, prompts, skills and local models
- private trading and operational systems where risk controls remain separate from model output

## Reviewer path

If you want the fastest high-signal inspection:

1. Read the [engineering dossier](ENGINEERING_DOSSIER.md).
2. Inspect the acceptance matrix in [hermes-reticulum-platform](https://github.com/fr3kchy/hermes-reticulum-platform/blob/main/docs/FINAL_ACCEPTANCE.md).
3. Inspect the security boundaries and tests in [hermes-fleet-control](https://github.com/fr3kchy/hermes-fleet-control).
4. Inspect the blocked-state discipline in [fr3k-infrastructure](https://github.com/fr3kchy/fr3k-infrastructure).
5. Reproduce the measured sweep in [qwen36-bench](https://github.com/fr3kchy/qwen36-bench).

**Contact:** `fr3k@mcpintelligence.com.au`
