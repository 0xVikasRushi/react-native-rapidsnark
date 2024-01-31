### MacOS Setup

1. Requires [Homebrew](https://brew.sh/), [git-lfs](https://git-lfs.com/) and Node.
2. Install dependencies:

```shell
brew install wget git-lfs
```

3. Fetch large files with git-lfs:

```shell
git lfs pull
```
4. Install [circom](https://github.com/iden3/circom).
5. Install [snarkjs](https://github.com/iden3/snarkjs). In case of errors try Node 18 with [n](https://www.npmjs.com/package/n/v/5.0.1) or other version manager.
    1. If you want to test inputs - place `input.json` file in internal `circuits` dir.
6. Run [`./scripts/build.sh`](./scripts/build.sh) to generate the circuit and witness files, prove and verify circuit with given `input.json`.
7. Run [`./scripts/copy_to_example.sh`](./scripts/copy_to_example.sh) to update example app with new circuit and witness files.

Current `circuit.circom` and  is taken from [iden3/circuits](https://github.com/iden3/circuits/blob/master/test/circuits/authV2Test.circom) repo.

Current `input.json` is taken from [iden3/circuits](https://github.com/iden3/circuits/blob/master/test/auth/authV2.test.ts) test, line 10.
