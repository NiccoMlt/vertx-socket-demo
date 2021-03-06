module.exports = {
  extends: [
    "airbnb-typescript",
    "airbnb/hooks",
    "plugin:@typescript-eslint/eslint-recommended",
    "plugin:@typescript-eslint/recommended",
    "plugin:@typescript-eslint/recommended-requiring-type-checking"
  ],
  parserOptions: {
    "project": "./tsconfig.json",
    "tsconfigRootDir": ".",
  },
  plugins: [
    "tsdoc"
  ],
  rules: {
    "react/prop-types": "off",
    "react/jsx-props-no-spreading": [
      "warn",
      {
        "html": "enforce",
        "custom": "ignore",
        "explicitSpread": "ignore"
      }
    ],
    "@typescript-eslint/explicit-function-return-type": [
      "warn",
      {
        allowExpressions: true,
      }
    ],
    "no-param-reassign": [
      "warn",
      {
        "props": false
      }
    ],
    "tsdoc/syntax": "warn",
    "no-console": "off",
    "no-restricted-syntax": [
      "error",
      {
        "selector": "CallExpression[callee.object.name='console'][callee.property.name!=/^(log|warn|error|info|trace)$/]",
        "message": "Unexpected property on console object was called"
      }
    ]
  }
};
