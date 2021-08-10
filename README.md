# pokeBattleSim

Desctop application to let two teams of classical pokemon fight against each other.

Frontend: Typescript

Backend: Java (Spring)

## Deployment

Ensure Docker is installed. Then run from repository root:

```bash
cd web && npm run build && cd ..
docker build -t poke-battle-sim .
docker run -p 8080:8080 poke-battle-sim
```
