# JAR2JAR
Ferramenta para manipulação de JAR

## Como unir dois JAR em um só:
Execute o seguinte comando:
```powershell
java -jar jar2jar.jar --jar=<firstjar> --jar2=<secondjar> --out=<jarOut> --target=<pathDecompile> --manifest=<manifest> --mode=unir
```
## Como remover um package do JAR:
```powershell
java -jar jar2jar.jar --jar=<firstjar> --out=<jarOut> --target=<pathDecompile> --manifest=<manifest> --ignorepackage=<package,package> --mode=remover
```
